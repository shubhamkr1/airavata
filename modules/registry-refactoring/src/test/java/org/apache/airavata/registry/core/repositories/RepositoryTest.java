/*
 *
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
*/
package org.apache.airavata.registry.core.repositories;

import org.apache.airavata.model.WorkflowModel;
import org.apache.airavata.model.experiment.ExperimentModel;
import org.apache.airavata.model.experiment.UserConfigurationDataModel;
import org.apache.airavata.model.user.NSFDemographics;
import org.apache.airavata.model.user.UserProfile;
import org.apache.airavata.model.workspace.Gateway;
import org.apache.airavata.model.workspace.GatewayApprovalStatus;
import org.apache.airavata.model.workspace.Notification;
import org.apache.airavata.model.workspace.Project;
import org.apache.airavata.registry.core.entities.expcatalog.ExperimentEntity;
import org.apache.airavata.registry.core.entities.workflowcatalog.WorkflowEntity;
import org.apache.airavata.registry.core.entities.workspacecatalog.GatewayEntity;
import org.apache.airavata.registry.core.entities.workspacecatalog.NotificationEntity;
import org.apache.airavata.registry.core.entities.workspacecatalog.ProjectEntity;
import org.apache.airavata.registry.core.entities.workspacecatalog.UserProfileEntity;
import org.apache.airavata.registry.core.repositories.expcatalog.ExperimentRepository;
import org.apache.airavata.registry.core.repositories.workflowcatalog.WorkflowRepository;
import org.apache.airavata.registry.core.repositories.workspacecatalog.GatewayRepository;
import org.apache.airavata.registry.core.repositories.workspacecatalog.NotificationRepository;
import org.apache.airavata.registry.core.repositories.workspacecatalog.ProjectRepository;
import org.apache.airavata.registry.core.repositories.workspacecatalog.UserProfileRepository;
import org.apache.airavata.registry.core.utils.ObjectMapperSingleton;
import org.dozer.Mapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class RepositoryTest {
    private final static Logger logger = LoggerFactory.getLogger(RepositoryTest.class);

    private GatewayRepository gatewayRepository;
    private NotificationRepository notificationRepository;
    private UserProfileRepository userProfileRepository;
    private ProjectRepository projectRepository;
    private ExperimentRepository experimentRepository;
    private WorkflowRepository workflowRepository;
    private String gatewayId;
    private String notificationId;
    private String userId;
    private String projectId;
    private String experimentId;
    private String templateId;

    private final String GATEWAY_DOMAIN = "test1.com";
    private final String NOTIFY_MESSAGE = "NotifyMe";
    private final String USER_COMMENT = "TestComment";
    private final String PROJECT_DESCRIPTION = "Test Description";
    private final String EXPERIMENT_NAME = "sample experiment";
    private final String EXPERIMENT_DESCRIPTION = "sample description";
    private final String WORKFLOW_NAME = "test Workflow";


    @Before
    public void setupRepository() {

        gatewayRepository = new GatewayRepository(Gateway.class, GatewayEntity.class);
        notificationRepository = new NotificationRepository(Notification.class,
                NotificationEntity.class);
        userProfileRepository = new UserProfileRepository(UserProfile.class, UserProfileEntity.class);
        projectRepository = new ProjectRepository(Project.class, ProjectEntity.class);
        experimentRepository = new ExperimentRepository(ExperimentModel.class, ExperimentEntity.class);
        workflowRepository = new WorkflowRepository(WorkflowModel.class, WorkflowEntity.class);

        gatewayId = "test.com" + System.currentTimeMillis();
        notificationId = UUID.randomUUID().toString();
        userId = "testuser" + System.currentTimeMillis();
        projectId = "project" + System.currentTimeMillis();
        experimentId = "exp" + System.currentTimeMillis();
        templateId = "templateId" + System.currentTimeMillis();
    }


    @Test
    public void gateWayRepositoryTest() {
        Gateway gateway = new Gateway();
        gateway.setGatewayApprovalStatus(GatewayApprovalStatus.ACTIVE);
        gateway.setGatewayId(gatewayId);

		/*
         * GateWay Repository Insert Operation Test
		 */
        gateway = gatewayRepository.create(gateway);
        Assert.assertTrue(!gateway.getGatewayId().isEmpty());

		/*
         * GateWay Repository Update Operation Test
		 */
        gateway.setDomain(GATEWAY_DOMAIN);
        gatewayRepository.update(gateway);
        gateway = gatewayRepository.get(gateway.getGatewayId());
        Assert.assertEquals(gateway.getDomain(), GATEWAY_DOMAIN);

		/*
         * GateWay Repository Select Operation Test
		 */
        gateway = null;
        gateway = gatewayRepository.get(gatewayId);
        Assert.assertNotNull(gateway);

		/*
         * GateWay Repository Delete Operation
		 */
        boolean deleteResult = gatewayRepository.delete(gatewayId);
        Assert.assertTrue(deleteResult);

    }

    @Test
    public void notificationRepositoryTest() {

        String tempNotificationId = null;
        Gateway gateway = new Gateway();
        gateway.setGatewayApprovalStatus(GatewayApprovalStatus.ACTIVE);
        gateway.setGatewayId(gatewayId);
        gateway.setDomain(GATEWAY_DOMAIN);
        gateway = gatewayRepository.create(gateway);

        Notification notification = new Notification();
        notification.setGatewayId(gateway.getGatewayId());
        notification.setNotificationId(notificationId);

		/*
         * Notification INSERT Operation Test
		 */
        notification = notificationRepository.create(notification);
        Assert.assertTrue(!notification.getNotificationId().isEmpty());

		/*
         * Notification SELECT Operation Test
		 */
        tempNotificationId = notification.getNotificationId();
        notification = null;
        notification = notificationRepository.get(tempNotificationId);
        Assert.assertNotNull(notification);


		/*
         * Notification UPDATE Operation Test
		 */
        notification.setNotificationMessage(NOTIFY_MESSAGE);
        notificationRepository.update(notification);
        notification = notificationRepository.get(notification.getNotificationId());
        Assert.assertEquals(NOTIFY_MESSAGE, notification.getNotificationMessage());

		/*
         * Notification DELETE Operation Test
		 */
        boolean result = notificationRepository.delete(tempNotificationId);
        Assert.assertTrue(result);

        gatewayRepository.delete(gatewayId);
    }

    @Test
    public void userProfileRepositoryTest() {

		/*
         * Creating Gateway required for UserProfile creation
		 */
        Gateway gateway = new Gateway();
        gateway.setGatewayApprovalStatus(GatewayApprovalStatus.ACTIVE);
        gateway.setGatewayId(gatewayId);
        gateway.setDomain(GATEWAY_DOMAIN);
        gateway = gatewayRepository.create(gateway);
        Assert.assertTrue(!gateway.getGatewayId().isEmpty());



		/*
         * UserProfile Instance creation
		 */
        UserProfile userProfile = new UserProfile();
        userProfile.setAiravataInternalUserId(userId);
        userProfile.setGatewayId(gateway.getGatewayId());

        /*
         * Workspace UserProfile Repository Insert Operation Test
		 */
        userProfile = userProfileRepository.create(userProfile);
        Assert.assertTrue(!userProfile.getAiravataInternalUserId().isEmpty());

        /*
         * Workspace UserProfile Repository Update Operation Test
		 */
        userProfile.setComments(USER_COMMENT);
        userProfileRepository.update(userProfile);
        userProfile = userProfileRepository.get(userId);
        System.out.println(userProfile.getComments());
        Assert.assertEquals(userProfile.getComments(), USER_COMMENT);

		/*
         * Workspace UserProfile Repository Select Operation Test
		 */
        userProfile = null;
        userProfile = userProfileRepository.get(userId);
        Assert.assertNotNull(userProfile);

		/*
         * Workspace UserProfile Repository Delete Operation
		 */
        boolean deleteResult = userProfileRepository.delete(userId);
        Assert.assertTrue(deleteResult);
        deleteResult = gatewayRepository.delete(gatewayId);
        Assert.assertTrue(deleteResult);


    }

    @Test
    public void projectRepositoryTest() {

		/*
         * Creating Gateway required for UserProfile & Project creation
		 */
        Gateway gateway = new Gateway();
        gateway.setGatewayApprovalStatus(GatewayApprovalStatus.ACTIVE);
        gateway.setGatewayId(gatewayId);
        gateway.setDomain(GATEWAY_DOMAIN);
        gateway = gatewayRepository.create(gateway);
        Assert.assertTrue(!gateway.getGatewayId().isEmpty());

		/*
         * UserProfile Instance creation required for Project Creation
		 */
        UserProfile userProfile = new UserProfile();
        userProfile.setAiravataInternalUserId(userId);
        userProfile.setGatewayId(gateway.getGatewayId());
        userProfile = userProfileRepository.create(userProfile);
        Assert.assertTrue(!userProfile.getAiravataInternalUserId().isEmpty());

        /*
         * Project Instance creation
         */
        Project project = new Project();
        project.setGatewayId(gatewayId);
        project.setOwner(userId);
        project.setProjectID(projectId);
        project.setGatewayIdIsSet(true);



        /*
         * Workspace Project Repository Insert Operation Test
		 */
        project = projectRepository.create(project);
        Assert.assertTrue(!project.getProjectID().isEmpty());



        /*
         * Workspace Project Repository Update Operation Test
		 */
        project.setDescription(PROJECT_DESCRIPTION);
        projectRepository.update(project);
        project = projectRepository.get(projectId);
        Assert.assertEquals(project.getDescription(), PROJECT_DESCRIPTION);

		/*
         * Workspace Project Repository Select Operation Test
		 */
        project = null;
        project = projectRepository.get(projectId);
        Assert.assertNotNull(project);

		/*
         * Workspace Project Repository Delete Operation
		 */
        boolean deleteResult = projectRepository.delete(projectId);
        Assert.assertTrue(deleteResult);

        deleteResult = userProfileRepository.delete(userId);
        Assert.assertTrue(deleteResult);

        deleteResult = gatewayRepository.delete(gatewayId);
        Assert.assertTrue(deleteResult);


    }

    @Test
    public void experimentRepositoryTest() {

		/*
         * Creating Gateway required for UserProfile & Project creation
		 */
        Gateway gateway = new Gateway();
        gateway.setGatewayApprovalStatus(GatewayApprovalStatus.ACTIVE);
        gateway.setGatewayId(gatewayId);
        gateway.setDomain(GATEWAY_DOMAIN);
        gateway = gatewayRepository.create(gateway);
        Assert.assertTrue(!gateway.getGatewayId().isEmpty());

		/*
         * UserProfile Instance creation required for Project Creation
		 */
        UserProfile userProfile = new UserProfile();
        userProfile.setAiravataInternalUserId(userId);
        userProfile.setGatewayId(gateway.getGatewayId());
        userProfile = userProfileRepository.create(userProfile);
        Assert.assertTrue(!userProfile.getAiravataInternalUserId().isEmpty());

        /*
         * Project Instance creation
         */
        Project project = new Project();
        project.setGatewayId(gatewayId);
        project.setOwner(userId);
        project.setProjectID(projectId);
        project.setGatewayIdIsSet(true);
        project = projectRepository.create(project);
        Assert.assertTrue(!project.getProjectID().isEmpty());

        /*
         * Experiment Instance Creation
         */

        ExperimentModel experiment = new ExperimentModel();
        experiment.setExperimentId(experimentId);
        experiment.setExperimentName(EXPERIMENT_NAME);
        experiment.setGatewayId(gatewayId);
        experiment.setUserName(userId);
        experiment.setProjectId(projectId);

        /*
         * Experiment Repository Insert Operation Test
		 */
        experiment = experimentRepository.create(experiment);
        Assert.assertTrue(!experiment.getExperimentId().isEmpty());




        /*
         * Experiment Repository Update Operation Test
		 */
        experiment.setDescription(EXPERIMENT_DESCRIPTION);
        experimentRepository.update(experiment);
        experiment = experimentRepository.get(experimentId);
        Assert.assertEquals(experiment.getDescription(), EXPERIMENT_DESCRIPTION);

		/*
         * Workspace Project Repository Select Operation Test
		 */
        experiment = null;
        experiment = experimentRepository.get(experimentId);
        Assert.assertNotNull(experiment);

		/*
         * Experiment Repository Delete Operation
		 */

        boolean deleteResult = experimentRepository.delete(experimentId);
        Assert.assertTrue(deleteResult);

        deleteResult = projectRepository.delete(projectId);
        Assert.assertTrue(deleteResult);

        deleteResult = userProfileRepository.delete(userId);
        Assert.assertTrue(deleteResult);

        deleteResult = gatewayRepository.delete(gatewayId);
        Assert.assertTrue(deleteResult);


    }


    @Test
    public void workflowRepositoryTest() {


        System.out.println();
		/*
         * Creating Gateway required for UserProfile & Workflow creation
		 */
        Gateway gateway = new Gateway();
        gateway.setGatewayApprovalStatus(GatewayApprovalStatus.ACTIVE);
        gateway.setGatewayId(gatewayId);
        gateway.setDomain(GATEWAY_DOMAIN);
        gateway = gatewayRepository.create(gateway);
        Assert.assertTrue(!gateway.getGatewayId().isEmpty());

		/*
         * UserProfile Instance creation required for Workflow Creation
		 */
        UserProfile userProfile = new UserProfile();
        userProfile.setAiravataInternalUserId(userId);
        userProfile.setGatewayId(gateway.getGatewayId());
        userProfile = userProfileRepository.create(userProfile);
        Assert.assertTrue(!userProfile.getAiravataInternalUserId().isEmpty());

        /*
         * Workflow Instance Creation
         */

        WorkflowModel workflowModel = new WorkflowModel();
        workflowModel.setTemplateId(templateId);
        workflowModel.setCreatedUser(userId);
        workflowModel.setGatewayId(gatewayId);
        workflowModel.setName(WORKFLOW_NAME);


        /*
         * Workflow Repository Insert Operation Test
		 */
        workflowModel = workflowRepository.create(workflowModel);
        Assert.assertTrue(!workflowModel.getTemplateId().isEmpty());




        /*
         * Workflow Repository Update Operation Test
		 */
        workflowModel.setGraph("test");
        workflowRepository.update(workflowModel);
        workflowModel = workflowRepository.get(templateId);
        Assert.assertEquals(workflowModel.getGraph(), "test");

		/*
         * Workflow Repository Select Operation Test
		 */
        workflowModel = null;
        workflowModel = workflowRepository.get(templateId);
        Assert.assertNotNull(workflowModel);

		/*
         * Workflow Repository Delete Operation
		 */

        boolean deleteResult = workflowRepository.delete(templateId);
        Assert.assertTrue(deleteResult);

        deleteResult = userProfileRepository.delete(userId);
        Assert.assertTrue(deleteResult);

        deleteResult = gatewayRepository.delete(gatewayId);
        Assert.assertTrue(deleteResult);


    }
}