package com.viettel.coreapi.model;

import java.util.HashSet;
import java.util.Set;

public final class Permissions {
    private Permissions() {
        //hidden constructor
    }

    public final static Set<String> ACTIONS = new HashSet<>();

    static {
        ACTIONS.add("account:create");
        ACTIONS.add("account:view-list");
        ACTIONS.add("account:delete");
        ACTIONS.add("account:update");
        ACTIONS.add("account:view-detail");
        ACTIONS.add("user:view-list");
        ACTIONS.add("user:view-detail");
        ACTIONS.add("user:update");
        ACTIONS.add("transaction:view-list");
        ACTIONS.add("transaction:view-detail");
        ACTIONS.add("configuration:view-list");
        ACTIONS.add("configuration:create");
        ACTIONS.add("configuration:delete");
        ACTIONS.add("configuration:view-detail");
        ACTIONS.add("box-definition:update");
        ACTIONS.add("box-definition:activate");
        ACTIONS.add("box-definition:approve");
        ACTIONS.add("box-definition:reset-action-limit-white-list");
    }

    public static final class Reward {
        public static final String IMPORT = "hasPermission('0', 'reward:import')";

        private Reward() {
            // hidden constructor
        }
    }

}
