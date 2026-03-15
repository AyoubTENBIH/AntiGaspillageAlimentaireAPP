package com.example.anti_gaspillagealimentaireapp.ui.auth;

import com.example.anti_gaspillagealimentaireapp.data.local.entities.UserRole;

public abstract class LoginNavigation {
    public static final class GoToRegister extends LoginNavigation {}
    public static final class GoToClient extends LoginNavigation {
        public final UserRole role;
        public GoToClient(UserRole role) { this.role = role; }
    }
    public static final class GoToMerchant extends LoginNavigation {
        public final UserRole role;
        public GoToMerchant(UserRole role) { this.role = role; }
    }
}
