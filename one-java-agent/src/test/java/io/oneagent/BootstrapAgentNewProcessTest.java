package io.oneagent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.TimeoutException;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.zeroturnaround.exec.InvalidExitValueException;
import org.zeroturnaround.exec.ProcessExecutor;
import org.zeroturnaround.exec.ProcessResult;

/**
 * 
 * @author hengyunabc 2020-12-18
 *
 */
public class BootstrapAgentNewProcessTest {

    private String runProcess() throws InvalidExitValueException, IOException, InterruptedException, TimeoutException {
        return runProcess(new Properties());
    }

    private String runProcess(Properties properties)
            throws InvalidExitValueException, IOException, InterruptedException, TimeoutException {
        File javaPath = ProcessUtils.findJava();

        String testClassesDir = this.getClass().getProtectionDomain().getCodeSource().getLocation().getFile();

        System.err.println(testClassesDir);

        File oneagentDir = new File(testClassesDir, "../oneagent");

        File oneagentJarFile = new File(oneagentDir, "one-java-agent.jar");

        String args = AgentArgsUtils.agentArgs();

        System.err.println("args: " + args);

        // java -javaagent:xxx.jar[=option] -cp [test-classes] TTT

        String agentStr = "-javaagent:" + oneagentJarFile.getCanonicalPath() + "=" + args;

        List<String> commands = new ArrayList<String>();
        commands.add(javaPath.getCanonicalPath());
        commands.add(agentStr);
        commands.add("-cp");
        commands.add(testClassesDir);

        for (Entry<Object, Object> entry : properties.entrySet()) {
            entry.getKey();
            commands.add("-D" + entry.getKey() + "=" + entry.getValue());
        }

        commands.add(TTT.class.getName());

        ProcessExecutor processExecutor = new ProcessExecutor().command(commands).readOutput(true);

        List<String> command = processExecutor.getCommand();

        StringBuilder sb = new StringBuilder();
        for (String str : command) {
            sb.append(str).append(' ');
        }

        System.err.println(sb.toString());

        ProcessResult result = processExecutor.execute();

        String outputString = result.outputString();
        return outputString;
    }

    @Test
    public void test() throws InvalidExitValueException, IOException, InterruptedException, TimeoutException {

        String processOutput = runProcess();
        System.err.println(processOutput);

        String className = "com.activator.test.DemoActivator";

        Assertions.assertThat(processOutput).contains("enabled " + className).contains("init " + className)
                .contains("start " + className).contains(TTT.STR);

        Assertions.assertThat(processOutput).contains("DemoAgent started.");

        // 测试加载 bytekit-demo-plugin 里的类
        Assertions.assertThat(processOutput).contains("bytekit-demo-plugin/target/bytekit-demo-plugin");

        // 测试加载 fastjson-demo-plugin 里的类
        Assertions.assertThat(processOutput).contains("DemoActivator: {\"name\":\"DemoActivator\"}");

        // 测试加载 demo-plugin 里加载的日志类路径
        Assertions.assertThat(processOutput).containsSubsequence("logger url:", "demo-plugin/target/demo-plugin",
                "logback-classic-1.2.9.jar");
        // 测试 demo-plugin 加载注入配置项
        Assertions.assertThat(processOutput).containsSubsequence("demoConfig: testValue", "nestConfig: nnn");
    }

    @Test
    public void testDisablePlugin()
            throws InvalidExitValueException, IOException, InterruptedException, TimeoutException {
        Properties properties = new Properties();
        properties.setProperty("oneagent.plugin." + "demo-plugin.enabled", "" + false);
        properties.setProperty("oneagent.plugin." + "demo-agent.enabled", "" + false);
        properties.setProperty("oneagent.verbose", "" + true);

        String processOutput = runProcess(properties);
        System.err.println(processOutput);

        String className = "com.activator.test.DemoActivator";

        Assertions.assertThat(processOutput).doesNotContain("enabled " + className).doesNotContain("init " + className)
                .doesNotContain("start " + className).contains(TTT.STR);
        Assertions.assertThat(processOutput).doesNotContain("DemoAgent started.");
    }
}
