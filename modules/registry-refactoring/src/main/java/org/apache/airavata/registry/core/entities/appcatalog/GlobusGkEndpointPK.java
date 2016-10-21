package org.apache.airavata.registry.core.entities.appcatalog;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the globus_gk_endpoint database table.
 * 
 */
@Embeddable
public class GlobusGkEndpointPK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	@Column(name="SUBMISSION_ID", insertable=false, updatable=false)
	private String submissionId;

	@Column(name="ENDPOINT")
	private String endpoint;

	public GlobusGkEndpointPK() {
	}

	public String getSubmissionId() {
		return submissionId;
	}

	public void setSubmissionId(String submissionId) {
		this.submissionId = submissionId;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof GlobusGkEndpointPK)) {
			return false;
		}
		GlobusGkEndpointPK castOther = (GlobusGkEndpointPK)other;
		return 
			this.submissionId.equals(castOther.submissionId)
			&& this.endpoint.equals(castOther.endpoint);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.submissionId.hashCode();
		hash = hash * prime + this.endpoint.hashCode();
		
		return hash;
	}
}