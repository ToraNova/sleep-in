
import mmls.MMLSclient;
import mmls.MMLSparser;
import mmls.MMLScourse;
import java.io.Console;
import java.util.List;
import java.io.File;

public class Sleepin{

  private static Console console = System.console();
  private static String sid;
  private static String pw;
  private static String cmdstr;
  private static List<MMLScourse> mainlist;
  private static MMLSparser mainparse;
  private static MMLSclient mmls0;

  public static void main(String [] args){
    credPrompt();
    mmls0 = new MMLSclient(sid,pw);
    mainparse = new MMLSparser();
    startScreen();
    while(true){
      cmdstr = console.readLine("Input: ");
      processCmd();
    }

	}

  private static void processCmd(){
    //main method of user interface
    char cmdf;
    int seltarget;
    int val0;
    int val1;
    MMLScourse selectCourse;
    if(new String(cmdstr).equals("help") || new String(cmdstr).equals("h")){
      printMenu();
    }else if(new String(cmdstr).equals("show")){
      printCourses();
    }else if(new String(cmdstr).equals("exit")){
      System.exit(0);
    }else{
      cmdf = cmdstr.charAt(0);
      try{
        seltarget = Integer.parseInt(cmdstr.split(",")[1]);
        selectCourse = mainlist.get(seltarget);
      }catch(Exception e){
        e.printStackTrace();
        System.out.println("Parsing cmd error! Please type the right command!");
        return;
      }
      switch(cmdf){
        case 's':
          try{
            val0 = Integer.parseInt(cmdstr.split(",")[2]);
            scanLoop(selectCourse,val0);
            selectCourse.saveFile(sid);
            return;
          }catch(Exception e){
            e.printStackTrace();
            System.out.println("Parsing cmd error! Please type the right command!");
            return;
          }
        case 'r':
          try{
            val0 = Integer.parseInt(cmdstr.split(",")[2]);
            val1 = Integer.parseInt(cmdstr.split(",")[3]);
            selectCourse.setRange(val0,val1);
            selectCourse.saveFile(sid);
            return;
          }catch(Exception e){
            e.printStackTrace();
            System.out.println("Parsing cmd error! Please type the right command!");
            return;
          }
        case 'q':
          try{
            val0 = Integer.parseInt(cmdstr.split(",")[2]);
            mainparse.setDoc(mmls0.signClass(selectCourse,val0));
            mainparse.parseSignIn();
            return;
          }catch(Exception e){
            e.printStackTrace();
            System.out.println("Parsing cmd error! Please type the right command!");
            return;
          }
        case 't':
        try{
          val0 = Integer.parseInt(cmdstr.split(",")[2]);
          selectCourse.setTarget(val0);
          selectCourse.saveFile(sid);
          return;
        }catch(Exception e){
          e.printStackTrace();
          System.out.println("Parsing cmd error! Please type the right command!");
          return;
        }
        case 'd':
          selectCourse.showLinksInstances();
          return;
        case 'p':
          selectCourse.writeLinksInstances();
          //print to excel here
          return;
        case 'c':
          selectCourse.refreshLinkInstances();
          return;
        default:
          System.out.println("Parsing cmd error! Please type the right command!");
          return;
      }
    }
  }

  private static void scanLoop(MMLScourse target,int num){
    int count =0;
    try{
      while( ! target.doneScan() && count < num ){
        count++;
        //performs a scans
        mainparse.setDoc(mmls0.signClass(target,target.getCurrentTarget())); //attempts the signin
        target.addLinkInstance(mainparse.parseSignInSilent()); //parse the result
      }
    }catch(Exception e){
      e.printStackTrace();
    }
  }

  private static void startScreen(){
    //show the start screen and configure the user data
    if(checkExist()){
      System.out.println("Previous records for "+sid+" are present.");
      mainlist = mainparse.readClassDat(sid);
    }else{
      System.out.println("No record for "+sid+", attempting to retrieve class data...");
      mainparse.setDoc(mmls0.getLogin());
      mainlist = mainparse.retrClassDat(sid);
      System.out.println("Retrieved class records from MMLS");
    }
    if(mainlist.size() < 1){
      System.out.println("Error has occurred, please make sure you enter the correct password");
      System.out.println("and also delete the directory data/"+sid+" before trying again.");
      return;
    }else{
      printCourses();
      System.out.println("Type help for the help guide");
    }
  }

  private static void printCourses(){
    //print the list of courses
    int count =0 ;
    for(MMLScourse c : mainlist){
      System.out.printf("______SELECTOR ID:__[%d]___________________________________________\n",count++);
      c.printStatus();
      System.out.println();
      System.out.println();
    }
  }

  private static Boolean checkExist(){
    //check if previous record of user exist
    File datdir = new File("data/"+sid);
    File[] listOfFiles = datdir.listFiles();
    if(listOfFiles == null){
      return false;
    }
    if(listOfFiles.length > 0){
      return true;
    }else{
      return false;
    }
  }

  private static void credPrompt(){
    //prompt for credentials
    sid = console.readLine("Please enter MMU Student ID for MMLS logins/signins: ");
    pw = new String(console.readPassword("Please enter password for %s: ",sid));
  }

  private static void printMenu(){
    //print the menu
    System.out.println("/-------------------------------------------------\\");
    System.out.println(" Sleepin v1 - MMU mmls attendance bypass");
    System.out.println(" email: chia_jason96@live.com");
    System.out.println(" v1 Release date: 12-23-2018");
    System.out.println(" github.com/ToraNova/sleep-in.git");
    System.out.println(" Welcome "+sid);
    System.out.println("---------------------------------------------------");
    System.out.println(" <x> is denoted as the selector. s<x> could be s0,s1 ...");
    System.out.println(" help - show this screen");
    System.out.println(" show - show the course status");
    System.out.println(" exit - quit the program");
    System.out.println(" s,<x>,<num> - start to scan n instances on x");
    System.out.println(" r,<x>,<start>,<end> - re-range x");
    System.out.println(" q,<x>,<num> - register for singular instance of x");
    System.out.println(" t,<x>,<num> - sets the current Scan number of x to num");
    System.out.println(" d,<x> - display QRLinkInstances of x on screen");
    System.out.println(" p,<x> - prints the QRLinkInstances of x onto a .csv file");
    System.out.println(" c,<x> - deletes all QRLinkInstances of x (irreversible)");
    System.out.println();
    System.out.println(" example: s,0,10 / r,1,0,100 / t,3,1544");
    System.out.println("\\-------------------------------------------------/");
  }
}
