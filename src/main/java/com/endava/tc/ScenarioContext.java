package com.endava.tc;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {
    private Map<String, Object> map = new HashMap();
    private static final ScenarioContext INSTANCE = new ScenarioContext();
    private ScenarioContext(){};
    public static ScenarioContext getInstance(){
        return INSTANCE;
    }
    public void saveData(String key, Object data){
        map.put(key, data);
    }

    public Object getData(String key){
        return map.get(key);
    }
}
