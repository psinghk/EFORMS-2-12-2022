/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.org.service;

import com.org.bean.MoveReqBean;
import com.org.bean.UserData;
import com.org.dao.FinalAuditDetails;
import com.org.dao.MoveReqDao;
import com.org.dto.StatusDetails;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Mohit sharma
 */
public class MoveReqService {

    //FinalAuditDetails finalAuditDetails = new FinalAuditDetails();
    MoveReqDao moveReqDao = new MoveReqDao();

    public List<FinalAuditDetails> getAllPendingRequsts(String email) {
        System.out.println("inside of service move request");
        return moveReqDao.getAllPendingRequsts(email);
    }

    public String updateRecords(List<FinalAuditDetails> AllDetails, String updateEmail,String status) {
        // String statusResponse = moveReqDao.insertIntoStatus();
        return moveReqDao.updateAllRecoads(AllDetails, updateEmail,status);
    }

    public String inserStatusRecords(List<FinalAuditDetails> AllDetails, String updateEmail, String roll, String hodMobile, UserData userData) throws ClassNotFoundException {
        // String statusResponse = moveReqDao.insertIntoStatus();
        return moveReqDao.insertIntoStatus(AllDetails, updateEmail, roll, hodMobile, userData);
    }

    public Map<String, String> fetchApplicantEmail(String empCategory, String cordMin, String cordDept) {
        return moveReqDao.fetchApplicantEmail(empCategory, cordMin, cordDept);
    }
}
