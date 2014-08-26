package mr.wordcount;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.hadoop.conf.Configuration;
import org.junit.Before;
import org.junit.Test;

public class StartsWithCountJobTest {

  /*
   * private File inputFile = new File("./target/test/input.txt"); private File
   * output = new File("./target/test-result/");
   */

  String work_dir = "home/impadmin/Documents/chekouts/hadoop_eg/HadoopSamples";

  private File inputFile = new File(work_dir + "/target/test/input.txt");
  private File output = new File(work_dir + "/target/test-result/");

  @Before
  public void setUpTest() throws IOException {
    FileUtils.deleteQuietly(inputFile);
    FileUtils.deleteQuietly(output);
    FileUtils.writeStringToFile(inputFile, "this is just a test, yes it is");
  }

  @Test
  public void testRun() throws Exception {
    System.out.println(inputFile.getAbsolutePath());
    System.out.println(output.getAbsolutePath());
    Configuration conf = new Configuration();
    conf.set("mapreduce.framework.name", "local");
    conf.set("fs.default.name", "file:///");

    StartsWithCountJob underTest = new StartsWithCountJob();
    underTest.setConf(conf);

    int exitCode =
        underTest.run(new String[] { inputFile.getAbsolutePath(),
            output.getAbsolutePath() });
    assertEquals("StartsWithCountJob returned error code. Please see the log.",
        0, exitCode);
    assertTrue(new File(output, "_SUCCESS").exists());
    Map<String, Integer> resAsMap =
        getResultAsMap(new File(output, "part-r-00000"));
    assertEquals(5, resAsMap.size());
    assertEquals(2, resAsMap.get("t").intValue());
    assertEquals(3, resAsMap.get("i").intValue());
    assertEquals(1, resAsMap.get("j").intValue());
    assertEquals(1, resAsMap.get("a").intValue());
    assertEquals(1, resAsMap.get("y").intValue());
  }

  private Map<String, Integer> getResultAsMap(File file) throws IOException {
    Map<String, Integer> result = new HashMap<String, Integer>();
    String contentOfFile = FileUtils.readFileToString(file);
    for (String line : contentOfFile.split("\n")) {
      String[] tokens = line.split("\t");
      result.put(tokens[0], Integer.parseInt(tokens[1]));
    }
    return result;
  }

}
