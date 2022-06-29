package com.example.disaster_message_notificator;

public class BehaviorInfo {
    private String disaster_keyword;
    private String behavior_example_link;

    public BehaviorInfo() { super(); }

    public BehaviorInfo(String disaster_name, String behavior_example_link) {
        this.disaster_keyword = disaster_name;
        this.behavior_example_link = behavior_example_link;
    }

    public String getDisaster_keyword() {
        return disaster_keyword;
    }

    public void setDisaster_keyword(String disaster_name) {
        this.disaster_keyword = disaster_name;
    }

    public String getBehavior_example_link() {
        return behavior_example_link;
    }

    public void setBehavior_example_link(String behavior_example_link) {
        this.behavior_example_link = behavior_example_link;
    }
}
