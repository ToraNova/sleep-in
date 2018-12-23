

import mmls.MMLSclient;
import mmls.MMLSparser;
import mmls.MMLScourse;
import java.io.Console;

public class ClassTester{

  public static Console testconsole = System.console();

  public static void main(String [] args){
    String sid = "1161300548";
    String pw = new String(testconsole.readPassword("Please enter password for %s: ",sid));
    MMLSclient mmls0 = new MMLSclient(sid,pw);
    MMLScourse target = MMLScourse.readFile("1161300548","368");

    MMLSparser mmps0 = new MMLSparser(mmls0.signClass(target,16121));
    mmps0.parseSignIn();
    // MMLSparser mmps0 = new MMLSparser("html","1161300548_231218[155924].html");
    // mmps0.testFunc();

	}
};
