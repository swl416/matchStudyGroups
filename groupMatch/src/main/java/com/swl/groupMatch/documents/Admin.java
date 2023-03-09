package com.swl.groupMatch.documents;

import com.swl.groupMatch.models.AdminId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("admins")
public class Admin {
    @Id
    private AdminId adminId;

    private String adminName;
    private String pw;

    public Admin(AdminId adminId, String adminName, String pw) {
        super();
        this.adminId = adminId;
        this.adminName = adminName;
        this.pw = pw;
    }

    public AdminId getAdminId() {
        return adminId;
    }

    public void setAdminId(AdminId adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }
}
