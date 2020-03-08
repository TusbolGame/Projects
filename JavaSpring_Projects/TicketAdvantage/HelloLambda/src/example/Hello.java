package example;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;

public class Hello implements RequestHandler<String, String> {

    @Override
    public String handleRequest(String input, Context context) {
        context.getLogger().log("Input: " + input);
        context.getLogger().log("\n context" + context);
        context.getLogger().log("\n getLogGroupName: " + context.getLogGroupName());
        context.getLogger().log("\n getLogStreamName: " + context.getLogStreamName());

        String output = "Hello, " + input + "!";
        return output;
    }
}
