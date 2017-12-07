import sys

sys.path.append('../lib')

from apache.airavata.api import Airavata
from apache.airavata.api.ttypes import *

from apache.airavata.model.workspace.ttypes import *
from apache.airavata.model.security.ttypes import AuthzToken
from apache.airavata.model.experiment.ttypes import *
from apache.airavata.model.appcatalog.appdeployment.ttypes import *
from apache.airavata.model.appcatalog.appinterface.ttypes import *
from apache.airavata.model.application.io.ttypes import *

import argparse
import configparser

from thrift import Thrift
from thrift.transport import TSSLSocket
from thrift.transport import TSocket
from thrift.transport import TTransport
from thrift.protocol import TBinaryProtocol

def get_transport(hostname, port):
    # Create a socket to the Airavata Server
    # TODO: validate server certificate
    transport = TSSLSocket.TSSLSocket(hostname, port, validate=False)

    # Use Buffered Protocol to speedup over raw sockets
    transport = TTransport.TBufferedTransport(transport)
    return transport

def get_airavata_client(transport):
    # Airavata currently uses Binary Protocol
    protocol = TBinaryProtocol.TBinaryProtocol(transport)

    # Create a Airavata client to use the protocol encoder
    airavataClient = Airavata.Client(protocol)
    return airavataClient

def get_authz_token(token,username,gatewayID):
    return AuthzToken(accessToken=token, claimsMap={'gatewayID': gatewayID, 'userName': username})  

def compute_resource(airavataClient,authz_token,gatewayID,username):
    computeResource = airavataClient.getComputeResource(authz_token,gatewayID,username,-1,0)
    return computeResource

if __name__ == '__main__':
    parser = argparse.ArgumentParser(description ="Get compute resource")
    parser.add_argument('compResId',type=str, help= "Compute Resource Id")
    
    args = parser.parse_args()
    print args

    config = configparser.RawConfigParser()
    config.read('../conf/airavata-client.properties')
    token = config.get('GatewayProperties', 'cred_token_id')

    username= config.get('AiravataServer', 'username')
    gatewayID = config.get('GatewayProperties', 'gateway_id')
    authz_token = get_authz_token(token,username,gatewayID)
    #print(authz_token)

    hostname = config.get('AiravataServer', 'host')
    port = config.get('AiravataServer', 'port')

    transport = get_transport(hostname, 9930)
    transport.open()
    airavataClient = get_airavata_client(transport)

    compResId = args.compResId

    computeResource = compute_resource(airavataClient,authz_token,compResId)
    print 'Compute Resource details: ', computeResource
    

    transport.close()