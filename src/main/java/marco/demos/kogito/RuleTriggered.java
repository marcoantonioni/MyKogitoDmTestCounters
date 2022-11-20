package marco.demos.kogito;

import java.util.HashMap;

public class RuleTriggered {

    private static boolean tracing = false;

    private static boolean getValue(HashMap<String,Object> params, String paramName) {
        boolean value = false;

        try {
            Boolean tempObj = (Boolean)params.get(paramName);
            value = tempObj.booleanValue();            
            if ( tracing ) System.out.println("        TROVATO: ["+paramName+"] = "+value);
        } catch (Throwable t) {
            // not present == ignore for AND logic
            value = true;
            if ( tracing ) System.out.println("        NON TROVATO: ["+paramName+"] defaulted to: "+value);
        }

        return value;
    }

    public static Boolean ruleTriggered(HashMap<String,Object> params) {
        
        String ruleName = "not-defined";
        boolean goldenRule = false;
        boolean param1 = false;
        boolean param2 = false;
        boolean param3 = false;

        try {
            ruleName = (String)params.get("ruleName");
        } catch (Throwable t) {
        }

        if ( tracing ) System.out.println("\n\n*******> Working on: ["+ruleName+"]");

        param1 = getValue(params, "param1");
        param2 = getValue(params, "param2");
        param3 = getValue(params, "param3");
        goldenRule = getValue(params, "goldenRule");

        if ( tracing ) System.out.println("===> Rule triggered: ["+ruleName+"] param1["+param1+"] param2["+param2+"] param3["+param3+"] goldenRule["+goldenRule+"]");
        if ( tracing ) System.out.println("     keys: "+params.keySet());
        if ( tracing ) System.out.println("     values: "+params.values());
        
        boolean finalValue = Boolean.valueOf(param1 & param2 & param3 & goldenRule);
        if ( tracing ) System.out.println("     RETURNING["+finalValue+"]");
        return finalValue;
    }
    
}
