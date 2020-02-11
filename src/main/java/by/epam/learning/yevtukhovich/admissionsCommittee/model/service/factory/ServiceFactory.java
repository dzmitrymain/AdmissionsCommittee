package by.epam.learning.yevtukhovich.admissionsCommittee.model.service.factory;

import by.epam.learning.yevtukhovich.admissionsCommittee.model.service.Service;

public class ServiceFactory {

    public static Service getService(ServiceType serviceType){
        return serviceType.getService();
    }
}
