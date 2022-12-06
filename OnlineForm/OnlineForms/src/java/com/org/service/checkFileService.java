package com.org.service;
import com.org.bean.DnsBean;
import com.org.bean.EmailBean;
import java.io.File;
import java.util.Set;
import validation.FileValidation;
/**
 *
 * @author nikki
 */
public class checkFileService {
    FileValidation filedao = new FileValidation();
    
    public Boolean checkPDF(File destFile) {
        return filedao.checkPDF(destFile);
    }
    public Boolean checkImage(File destFile) {
        return filedao.checkImage(destFile);
    }
    
    // create dao and validation from esigning
    public Boolean checkExcel(File destFile) {
        return filedao.checkExcel(destFile);
    }
    public Boolean checkCSV(File destFile) {
        return filedao.checkCSV(destFile);
    }
    
    public Set<DnsBean> checkCSVForDNS(File destFile, String req_type, String req_other_type) {
        return filedao.checkCSVForDNS(destFile, req_type, req_other_type);
    }
    
    public Set<EmailBean> checkCSVForBULK(File destFile) {
        return filedao.checkCSVForBULK(destFile);
    }
}
