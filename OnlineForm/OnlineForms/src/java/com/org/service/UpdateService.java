package com.org.service;

import admin.UserTrack;
import com.org.dao.ProfileDao;
import java.util.Set;
import model.FormBean;
// file created by pr on 1stfeb18

public class UpdateService {

    UserTrack usertrack = new UserTrack();
    ProfileDao profileDao = new ProfileDao();

    // below function added by pr on 1stfeb18
    public Boolean prevUpdateService(FormBean form_details, String ref, String admin_role, String filename, String email, Set<String> aliases) {

        return usertrack.PreviewUpdateRequest1(form_details, ref, admin_role, filename, email, aliases);
    }

    public int update_profile(FormBean form_details, String email) {
        return profileDao.update_profile(form_details, email);
    }
}
