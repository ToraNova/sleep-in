package mmls;
//handles arrays of MMLSposts

import java.util.List;
import java.util.ArrayList;

public class Posthandler{

  private List<MMLSpost> postlist;

  public Posthandler(){
    //default constructor
    postlist = new ArrayList<MMLSpost>();
  }

  public void addPost(MMLSpost target){
    postlist.add(target);
  }

}
