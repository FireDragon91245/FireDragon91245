import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class FactoryV6 extends PApplet {

//Variables (Do not change)
float mainData[] = new float[100];
boolean options[] = new boolean[10];
PImage manuelTextures[] = new PImage[10];
PImage autoTextures[][] = new PImage[20][5];
String autoTexturesTxt[][] = new String[20][5];
bHandler bh;
gHandler gh;
iHandler ih;

//Variables (Changable)
int oreCount = 1000;
int gridSize = 100;
int minOre = 20;
int maxOre = 40;

//Setup
public void setup(){
  frameRate(80);
  options[0] = true;
  options[1] = true;
  //size(displayWidth,displayHeight,P2D);
  
  bh = new bHandler();
  gh = new gHandler();
  ih = new iHandler();
  mainData[10] = 1;
  gh.loadGraphicLibari();
  bh.oreGen();
  gh.imageRegister();
  bh.loadConfig();
}

//Draw
public void draw(){
  background(255);
  gh.zoom();
  gh.oreRender();
  gh.ofset();
  gh.mousePreeLacation();
  if(keyPressed){
    gh.gridRender();
  }
  gh.bMenuRender();
  gh.toolTips();
  gh.statsMenu();
  ih.inventar();
  mainData[4] = round(((mouseX - mainData[2]) / mainData[0])-0.5f);
  mainData[5] = round(((mouseY - mainData[3]) / mainData[0])-0.5f);
  mainData[7]++;
  if(mainData[7] >= 60){
    mainData[7] = 0;
    ih.mainItem();
  }
}
public void keyPressed(){
  gh.statsMenuTogle();
  gh.reset();
  bh.rotate();
  bh.delete();
}
public void mousePressed(){
  bh.build();
  bh.buildSelect();
}
//Variables
int buildData[][][] = new int[gridSize][gridSize][10];
String TTConfigTxt[][] = new String[20][2];
int IDConfigTxt[][] = new int[20][3];
String ITConfigTxt[][] = new String[20][3];
int freeSaveSlot;
Table TTConfig;
Table IDConfig;
Table ITConfig;

//Main Class
class bHandler{
  
  //Generate Ores in Random Positions
  public void oreGen(){
    int x, y, id, count;
    for(int i = 0; i < oreCount; i++){
      x = round(random(0,gridSize-1));
      y = round(random(0,gridSize-1));
      id = round(random(1,3));
      count = round(random(minOre,maxOre));
      if(buildData[x][y][0] == 0){
        buildData[x][y][0] = 1;
        buildData[x][y][2] = id;
        buildData[x][y][3] = count;
        print("X: " + x + " Y: " + y + " ID: " + id + "      II      ");
      }
    }
  }
  
  //Building core
  public void build(){
    if(mouseX > 0 && mouseX < width){
      if(mouseY > 0 && mouseY < height-200){
        if(mainData[4] >= 0 && mainData[4] < gridSize && mainData[5] >= 0 && mainData[5] < gridSize){
          if(buildData[round(mainData[4])][round(mainData[5])][0] == 0){
            buildData[round(mainData[4])][round(mainData[5])][0] = round(mainData[9]);
            buildData[round(mainData[4])][round(mainData[5])][1] = round(mainData[10]);
          }
        }
      }
    }
  }
  
  //Select wich Building Menu is Shown &  Options & Selected Building
  public void buildSelect(){
    if(mouseX > 0 && mouseX < 50){
      if(mouseY > height-180 && mouseY < height-130){
        mainData[8] = 1;
      }
      if(mouseY > height-130 && mouseY < height-80){
        mainData[8] = 2;
      }
    }
    if(mouseY > height-170 && mouseY < height-120){
      if(mainData[8] == 1){
        if(mouseX > 60 && mouseX < 110){
          mainData[9] = 2;
        }
      }else if(mainData[8] == 2){
        if(mouseX > 60 && mouseX < 110){
          mainData[9] = 4;
        }
        if(mouseX > 120 && mouseX < 170){
          mainData[9] = 3;
        }
      }
    }
  }
  
  //Switch Rotation for building
  public void rotate(){
    if(key == 'r'){
      if(mainData[10] == 4){
        mainData[10] = 1;
      }else{
        mainData[10]++;
      }
    }
  }
  public void loadConfig(){
    TTConfig = loadTable("Data//tooltip_config.csv", "header");
    IDConfig = loadTable("Data//idBuilding.csv", "header");
    ITConfig = loadTable("Data//idItem.csv", "header");
    for(int i = 0; i < 20; i++){
      TableRow row1 = TTConfig.getRow(i);
      TTConfigTxt[i][0] = row1.getString("txt1");
      TTConfigTxt[i][1] = row1.getString("txt2");
      println(TTConfigTxt[i][0],TTConfigTxt[i][1]);
      TableRow row2 = IDConfig.getRow(i);
      IDConfigTxt[i][0] = row2.getInt("maxCapacity");
      IDConfigTxt[i][1] = row2.getInt("baseTransferSpeed");
      IDConfigTxt[i][2] = row2.getInt("Slots");
      println(IDConfigTxt[i][0],IDConfigTxt[i][1]);
      TableRow row3 = ITConfig.getRow(i);
      ITConfigTxt[i][0] = row3.getString("name");
      ITConfigTxt[i][1] = row3.getString("color");
      ITConfigTxt[i][2] = row3.getString("textureOnOre");
      println(ITConfigTxt[i][0],ITConfigTxt[i][1],ITConfigTxt[i][2]);
    }
  }
  public void delete(){
    if(keyCode == 8){
      buildData[round(mainData[4])][round(mainData[5])][0] = 0;
      buildData[round(mainData[4])][round(mainData[5])][1] = 0;
      buildData[round(mainData[4])][round(mainData[5])][2] = 0;
      buildData[round(mainData[4])][round(mainData[5])][3] = 0;
    }
  }
}
//Variables
int colors[][] = new int[20][8];
PFont fonts[] = new PFont[10];
Table loadColors;

//Main Graphic Class
class gHandler{
  int mainColor;
  PImage mainImage;
  String txt[] = {"","",""};
  //Render Ores
  public void oreRender(){
    boolean render = true;
    for(int x = 0;x < gridSize; x++){
      for(int y = 0; y < gridSize; y++){
        if(buildData[x][y][0] > 0){
          for(int i = 0; i < 20; i++){
            if(buildData[x][y][0] == colors[i][1]){
              mainColor = color(colors[i][2],colors[i][3],colors[i][4],colors[i][5]);
              if(colors[i][7] == 1){
                render = false;
              }else{
                render = true;
              }
              if(buildData[x][y][1] > 0){
                mainImage = autoTextures[buildData[x][y][0]][buildData[x][y][1]];
              }else{
                mainImage = null;
              }
            }
          }
          if(render == true){
            fill(mainColor);
            rect(x*mainData[0]+mainData[2],y*mainData[0]+mainData[3],mainData[0],mainData[0]);
          }
          if(mainImage != null){
            image(mainImage,x*mainData[0]+mainData[2]+(mainData[0]/2),y*mainData[0]+mainData[3]+(mainData[0]/2),mainData[0],mainData[0]);
          }
        }
      }
    }
  }
  
  //Zoom
  public void zoom(){
     mainData[0] = 10 * (mainData[1] + 1);
     if(keyPressed){
       if(key == '+'){
         if(mainData[1] < 40){
           mainData[1] = mainData[1] + 0.1f;
           println("Zoom: "+mainData[1]);
         }
       }
       if(key == '-'){
         if(mainData[1] > -0.9f){
           mainData[1] = mainData[1] - 0.1f;
           println("Zoom: "+mainData[1]);
         }
       }
     }
  }
  
  //Render grid (G)
  public void gridRender(){
    if(key == 'g'){
      for(float i = mainData[3];i < (mainData[0] * (gridSize+1))+mainData[3]-1;i = i + mainData[0]){
        line(0+mainData[2],i,(mainData[0] * gridSize)+mainData[2],i);
      }
      for(float i = mainData[2];i < (mainData[0] * (gridSize+1))+mainData[2]-1;i = i + mainData[0]){
        line(i,0+mainData[3],i,(mainData[0] * gridSize)+mainData[3]);
      }
    }
  }
  
  public void ofset(){
    if(keyPressed){
      if(keyCode == UP){
        mainData[3] = mainData[3] + 5;
      }
      if(keyCode == DOWN){
        mainData[3] = mainData[3] - 5;
      }
      if(keyCode == LEFT){
        mainData[2] = mainData[2] + 5;
      }
      if(keyCode == RIGHT){
        mainData[2] = mainData[2] - 5;
      }
    }
  }
  
  //Reset Scren POS & Zoom
  public void reset(){
     if(key == 'ü'){
       mainData[2] = 0;
       mainData[3] = 0;
       mainData[1] = 0;
     }
  }
  
  //Load color register
  public void loadGraphicLibari(){
    loadColors = loadTable("render_config.csv", "header");
    for(int i = 0;i < 20;i++){
        TableRow row = loadColors.getRow(i);
        colors[i][0] = row.getInt("primerKey");
        colors[i][1] = row.getInt("id");
        colors[i][2] = row.getInt("R");
        colors[i][3] = row.getInt("G");
        colors[i][4] = row.getInt("B");
        colors[i][5] = row.getInt("transparency");
        colors[i][7] = row.getInt("render");
        colors[i][6] = row.getInt("textureID");
        autoTexturesTxt[colors[i][1]][colors[i][6]] = row.getString("texture");
        println("ID: "+colors[i][1] +"  R: "+colors[i][2] + "  G: "+colors[i][3] + "  B: " + colors[i][4] + " transparency: " + colors[i][5]);
        println(autoTexturesTxt[colors[i][1]][colors[i][6]] + "       " + colors[i][6]);
    }
  }
  public void mousePreeLacation(){
    if(mouseX > 0 && mouseX < width){
      if(mouseY > 0 && mouseY < height-200){
        if(options[0] == true){
          fill(0,255,0,100);
          rect(mainData[4]*mainData[0]+mainData[2],mainData[5]*mainData[0]+mainData[3],mainData[0],mainData[0]);
        }
      }
    }
  }
  //Load and resize Images & Load Fonts
  public void imageRegister(){
    imageMode(CENTER);
    manuelTextures[0] = loadImage("graphics//options_gear.png");
    manuelTextures[0].resize(50,50);
    manuelTextures[1] = loadImage("graphics//mining_pickaxe.png");
    manuelTextures[1].resize(50,50);
    manuelTextures[2] = loadImage("graphics//transport_conveyor.png");
    manuelTextures[2].resize(50,50);
    manuelTextures[3] = loadImage("graphics//mining_pickaxe_selected.png");
    manuelTextures[3].resize(50,50);
    manuelTextures[4] = loadImage("graphics//transport_conveyor_selected.png");
    manuelTextures[4].resize(50,50);
    manuelTextures[5] = loadImage("graphics//warehouse_symbol.png");
    manuelTextures[5].resize(50,50);
    manuelTextures[6] = loadImage("graphics//warehouse_symbol_selected.png");
    manuelTextures[6].resize(50,50);
    
    //Fonts
    fonts[0] = loadFont("data//font//font_1.vlw");
    fonts[1] = loadFont("data//font//font_2.vlw");
    
    //Auto Load
    for(int i = 0; i < 20;i++){
      if(colors[i][6] != 0){
        autoTextures[colors[i][1]][colors[i][6]] = loadImage(autoTexturesTxt[colors[i][1]][colors[i][6]]);
      }
    }
  }
  
  //Rendering of The Build Menu
  public void bMenuRender(){
    beginShape();
    fill(0);
    vertex(0,height-200);
    fill(50,0,0);
    vertex(width,height-200);
    fill(75,75,75);
    vertex(width,height);
    fill(50,100,100);
    vertex(0,height);
    fill(75,25,25);
    vertex(0,height-200);
    fill(0,0,0);
    vertex(50,height-200);
    fill(100,100,100);
    vertex(50,height-20);
    fill(50,50,50);
    vertex(width-50,height-20);
    fill(50,0,0);
    vertex(width-50,height-180);
    fill(0,0,0);
    vertex(0,height-180);
    endShape();
    fill(0,0,0,200);
    rect(50,height-180,dist(50,0,width-50,0),dist(0,height-180,0,height-20));
    image(manuelTextures[0],width-25,height-75);
    if(mainData[8] == 1){
      image(manuelTextures[3],25,height-155);
      if(mainData[9] == 2){
        image(manuelTextures[3],85,height-145);
      }else{
        image(manuelTextures[1],85,height-145);
      }
    }else{
      image(manuelTextures[1],25,height-155);
    }
    if(mainData[8] == 2){
      image(manuelTextures[4],25,height-105);
      if(mainData[9] == 4){
        image(manuelTextures[4],85,height-145);
      }else{
        image(manuelTextures[2],85,height-145);
      }
      if(mainData[9] == 3){
        image(manuelTextures[6],145,height-145);
      }else{
        image(manuelTextures[5],145,height-145);
      }
    }else{
      image(manuelTextures[2],25,height-105);
    }
  }
  
  //Display a description of the selected item
  public void toolTips(){
    //always tooltips
    if(mainData[10] == 1){
      txt[2] = round(mainData[10])+": Up";
    }else if(mainData[10] == 2){
      txt[2] = round(mainData[10])+": Down";
    }else if(mainData[10] == 3){
      txt[2] = round(mainData[10])+": Right";
    }else if(mainData[10] == 4){
      txt[2] = round(mainData[10])+": Left";
    }
    if(mainData[9] == 4){
      textFont(fonts[0],30);
      text(txt[2],width/2,50);
    }
    
    //enabled tooltips
    if(TTConfigTxt[round(mainData[9])][0].matches("none") == false){
      txt[0] = TTConfigTxt[round(mainData[9])][0];
      txt[1] = TTConfigTxt[round(mainData[9])][1];
    }
    
    if(options[1] == true){
      textFont(fonts[0],25);
      fill(255,0,0);
      text(txt[0],10,height-230);
      fill(0);
      text(txt[1],10,height-210);
      textSize(200);
    }
  }
  
  //Togle stats menu on off
  public void statsMenuTogle(){
    if(keyCode == 99){
      if(options[2] == false){
        options[2] = true;
      }else{
        options[2] = false;
      }
    }
  }
  
  //printing stats menu
  public void statsMenu(){
    float f3[] = new float[4];
    if(options[2] == true){
      textFont(fonts[1],30);
      f3[0] = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / 1000000;
      f3[1] = Runtime.getRuntime().totalMemory() / 1000000;
      text("RAM: " + f3[0] + "MB / " + f3[1] + "MB -- "+round((f3[0]*100)/f3[1])+"%",20,50);
      text("FPS: " + round(frameRate) + "fps",20,100);
      for(int x = 0; x < gridSize; x++){
        for(int y = 0; y < gridSize; y++){
          if(buildData[x][y][0] > 0){
            f3[2]++;
          }
        }
      }
      text("UsedSaveSlots: " + round(f3[2]) + "slots / " + gridSize*gridSize + "slots -- " + (f3[2]*100)/(gridSize*gridSize) + "%",20,150);
      text("Resulution: " + displayWidth + "p x " + displayHeight + "p",20,200);
    }
  }
  
  //Render inventar (recources from item handler)
  public void invRender(int id1, int count1, int id2, int count2, int id3, int count3){
    if(id1 != 0 || id2 != 0 || id3 != 0){
      fill(100,100,100,200);
      rect(mouseX+20,mouseY,200,90);
      textFont(fonts[0],20);
      fill(255,0,0);
      text("Resources",mouseX+25,mouseY+20);
      for(int i = 0; i < 20; i++){
        if(ITConfigTxt[id1][0].matches("none") == false && id1 != 0){
          fill(unhex(ITConfigTxt[id1][1]));
          text(ITConfigTxt[id1][0] + ": " + count1,mouseX+25,mouseY+40);
        }
        if(ITConfigTxt[id2][0].matches("none") == false && id2 != 0){
          fill(unhex(ITConfigTxt[id2][1]));
          text(ITConfigTxt[id2][0] + ": " + count2,mouseX+25,mouseY+60);
        }
        if(ITConfigTxt[id3][0].matches("none") == false && id3 != 0){
          fill(unhex(ITConfigTxt[id3][1]));
          text(ITConfigTxt[id3][0] + ": " + count3,mouseX+25,mouseY+80);
        }
      }
    }
  }
}
class iHandler{
  public void inventar(){
    if(mainData[4] >= 0 && mainData[4] < gridSize && mainData[5] >= 0 && mainData[5] < gridSize){
      //println(buildData[round(mainData[4])][round(mainData[5])][0],buildData[round(mainData[4])][round(mainData[5])][1],buildData[round(mainData[4])][round(mainData[5])][2],buildData[round(mainData[4])][round(mainData[5])][3],buildData[round(mainData[4])][round(mainData[5])][4],buildData[round(mainData[4])][round(mainData[5])][5],buildData[round(mainData[4])][round(mainData[5])][6],buildData[round(mainData[4])][round(mainData[5])][7]);
      gh.invRender(buildData[round(mainData[4])][round(mainData[5])][2],buildData[round(mainData[4])][round(mainData[5])][3],buildData[round(mainData[4])][round(mainData[5])][4],buildData[round(mainData[4])][round(mainData[5])][5],buildData[round(mainData[4])][round(mainData[5])][6],buildData[round(mainData[4])][round(mainData[5])][7]);
    }
  }
  public void mainItem(){
    for(int x = 0; x < gridSize; x++){
      for(int y = 0; y < gridSize; y++){
        if(buildData[x][y][0] == 2){
          addItemMine(x,y);
        }else if(buildData[x][y][0] == 4){
          addItemConveyor(x,y);
        }
      }
    }
  }
  private void addItemMine(int x, int y){
    int x2[] = {x,x,x+1,x-1};
    int y2[] = {y-1,y+1,y,y};
    for(int i = 0; i < 4; i++){
      if(buildData[x2[i]][y2[i]][0] == 1){
        int slot = getMineSlot(x,y,x2[i],y2[i]);
        if(slot != -1){
          if(buildData[x2[i]][y2[i]][3] >= IDConfigTxt[buildData[x][y][0]][1]){
            buildData[x][y][slot+1]+=IDConfigTxt[2][1];
            buildData[x2[i]][y2[i]][3]-=IDConfigTxt[2][1];
            if(buildData[x][y][slot] == 0){
              buildData[x][y][slot] = buildData[x2[i]][y2[i]][2];
            }
          }else{
            buildData[x2[i]][y2[i]][0] = 0;
            buildData[x2[i]][y2[i]][1] = 0;
            buildData[x2[i]][y2[i]][2] = 0;
            buildData[x2[i]][y2[i]][3] = 0;
          }
        }
      }
    }
  }
  public int getMineSlot(int x, int y, int x2, int y2){
    int s = 0; int fs = 0;
    if(buildData[x][y][2] == 0){
      fs = 2;
    }else if(buildData[x][y][4] == 0){
      fs = 4;
    }else if(buildData[x][y][6] == 0){
      fs = 6;
    }else{
      fs = -1;
    }
    if(buildData[x][y][2] == buildData[x2][y2][2]){
      s = 2;
    }else if(buildData[x][y][4] == buildData[x2][y2][2]){
      s = 4;
    }else if(buildData[x][y][6] == buildData[x2][y2][2]){
      s = 6;
    }else if(fs != 1){
      s = fs;
    }else{
      s = -1;
    }
    return s;
  }
  private void addItemConveyor(int x, int y){
    int otherInS = 0;
    int otherOutS = 0;
    if(buildData[x][y][2] > 0 && buildData[x][y][3] <= 0){
      buildData[x][y][2] = 0;
      buildData[x][y][3] = 0;
    }
    if(buildData[x][y][1] == 1 && buildData[x][y+1][0] != 0){
      otherInS = getOtherInConveyorSlot(x,y,x,y+1);
      if(otherInS != -1){
        if(buildData[x][y+1][otherInS+1] >= IDConfigTxt[buildData[x][y][0]][1]){
          if(buildData[x][y][3] < IDConfigTxt[buildData[x][y][0]][0]){
            buildData[x][y][3]+=IDConfigTxt[buildData[x][y][0]][1];
            buildData[x][y+1][otherInS+1]-=IDConfigTxt[buildData[x][y][0]][1];
            if(buildData[x][y][2] == 0){
              buildData[x][y][2] = buildData[x][y+1][otherInS];
            }
          }
        }else{
          if(buildData[x][y][3] < IDConfigTxt[buildData[x][y][0]][0]){
            buildData[x][y][3]+=buildData[x][y+1][otherInS+1];
            if(buildData[x][y][2] == 0){
              buildData[x][y][2] = buildData[x][y+1][otherInS];
            }
            buildData[x][y+1][otherInS+1] = 0;
            buildData[x][y+1][otherInS] = 0;
          }
        }
      }
      otherOutS = getOtherOutConveyorSlot(x,y,x,y-1);
      if(otherOutS != -1){
        if(buildData[x][y-1][otherOutS+1] < IDConfigTxt[buildData[x][y-1][0]][0]){
          if(buildData[x][y][3] >= IDConfigTxt[buildData[x][y][0]][1]){
            buildData[x][y][3]-=IDConfigTxt[buildData[x][y][0]][1];
            buildData[x][y-1][otherOutS+1]+=IDConfigTxt[buildData[x][y][0]][1];
            if(buildData[x][y-1][otherOutS] == 0){
              buildData[x][y-1][otherOutS] = buildData[x][y][2];
            }
          }else{
            buildData[x][y-1][otherOutS+1]+=buildData[x][y][3];
            if(buildData[x][y-1][otherOutS] == 0){
              buildData[x][y-1][otherOutS] = buildData[x][y][2];
            }
            buildData[x][y][2] = 0;
            buildData[x][y][3] = 0;
          }
        }
      }
    }else if(buildData[x][y][1] == 2 && buildData[x][y-1][0] != 0){
      otherInS = getOtherInConveyorSlot(x,y,x,y-1);
      if(otherInS != -1){
        if(buildData[x][y-1][otherInS+1] >= IDConfigTxt[buildData[x][y][0]][1]){
          if(buildData[x][y][3] < IDConfigTxt[buildData[x][y][0]][0]){
            buildData[x][y][3]+=IDConfigTxt[buildData[x][y][0]][1];
            buildData[x][y-1][otherInS+1]-=IDConfigTxt[buildData[x][y][0]][1];
            if(buildData[x][y][2] == 0){
              buildData[x][y][2] = buildData[x][y-1][otherInS];
            }
          }
        }else{
          if(buildData[x][y][3] < IDConfigTxt[buildData[x][y][0]][0]){
            buildData[x][y][3]+=buildData[x][y-1][otherInS+1];
            if(buildData[x][y][2] == 0){
              buildData[x][y][2] = buildData[x][y-1][otherInS];
            }
            buildData[x][y-1][otherInS+1] = 0;
            buildData[x][y-1][otherInS] = 0;
          }
        }
      }
      otherOutS = getOtherOutConveyorSlot(x,y,x,y+1);
      if(otherOutS != -1){
        if(buildData[x][y+1][otherOutS+1] < IDConfigTxt[buildData[x][y+1][0]][0]){
          if(buildData[x][y][3] >= IDConfigTxt[buildData[x][y][0]][1]){
            buildData[x][y][3]-=IDConfigTxt[buildData[x][y][0]][1];
            buildData[x][y+1][otherOutS+1]+=IDConfigTxt[buildData[x][y][0]][1];
            if(buildData[x][y+1][otherOutS] == 0){
              buildData[x][y+1][otherOutS] = buildData[x][y][2];
            }
          }else{
            buildData[x][y+1][otherOutS+1]+=buildData[x][y][3];
            if(buildData[x][y+1][otherOutS] == 0){
              buildData[x][y+1][otherOutS] = buildData[x][y][2];
            }
            buildData[x][y][2] = 0;
            buildData[x][y][3] = 0;
          }
        }
      }
    }else if(buildData[x][y][1] == 3 && buildData[x-1][y][0] != 0){
      otherInS = getOtherInConveyorSlot(x,y,x-1,y);
      if(otherInS != -1){
        if(buildData[x-1][y][otherInS+1] >= IDConfigTxt[buildData[x][y][0]][1]){
          if(buildData[x][y][3] < IDConfigTxt[buildData[x][y][0]][0]){
            buildData[x][y][3]+=IDConfigTxt[buildData[x][y][0]][1];
            buildData[x-1][y][otherInS+1]-=IDConfigTxt[buildData[x][y][0]][1];
            if(buildData[x][y][2] == 0){
              buildData[x][y][2] = buildData[x-1][y][otherInS];
            }
          }
        }else{
          if(buildData[x][y][3] < IDConfigTxt[buildData[x][y][0]][0]){
            buildData[x][y][3]+=buildData[x-1][y][otherInS+1];
            if(buildData[x][y][2] == 0){
              buildData[x][y][2] = buildData[x-1][y][otherInS];
            }
            buildData[x-1][y][otherInS+1] = 0;
            buildData[x-1][y][otherInS] = 0;
          }
        }
      }
      otherOutS = getOtherOutConveyorSlot(x,y,x+1,y);
      if(otherOutS != -1){
        if(buildData[x+1][y][otherOutS+1] < IDConfigTxt[buildData[x+1][y][0]][0]){
          if(buildData[x][y][3] >= IDConfigTxt[buildData[x][y][0]][1]){
            buildData[x][y][3]-=IDConfigTxt[buildData[x][y][0]][1];
            buildData[x+1][y][otherOutS+1]+=IDConfigTxt[buildData[x][y][0]][1];
            if(buildData[x+1][y][otherOutS] == 0){
              buildData[x+1][y][otherOutS] = buildData[x][y][2];
            }
          }else{
            buildData[x+1][y][otherOutS+1]+=buildData[x][y][3];
            if(buildData[x+1][y][otherOutS] == 0){
              buildData[x+1][y][otherOutS] = buildData[x][y][2];
            }
            buildData[x][y][2] = 0;
            buildData[x][y][3] = 0;
          }
        }
      }
    }else if(buildData[x][y][1] == 4 && buildData[x-1][y][0] != 0){
      otherInS = getOtherInConveyorSlot(x,y,x+1,y);
      if(otherInS != -1){
        if(buildData[x+1][y][otherInS+1] >= IDConfigTxt[buildData[x][y][0]][1]){
          if(buildData[x][y][3] < IDConfigTxt[buildData[x][y][0]][0]){
            buildData[x][y][3]+=IDConfigTxt[buildData[x][y][0]][1];
            buildData[x+1][y][otherInS+1]-=IDConfigTxt[buildData[x][y][0]][1];
            if(buildData[x][y][2] == 0){
              buildData[x][y][2] = buildData[x+1][y][otherInS];
            }
          }
        }else{
          if(buildData[x][y][3] < IDConfigTxt[buildData[x][y][0]][0]){
            buildData[x][y][3]+=buildData[x+1][y][otherInS+1];
            if(buildData[x][y][2] == 0){
              buildData[x][y][2] = buildData[x+1][y][otherInS];
            }
            buildData[x+1][y][otherInS+1] = 0;
            buildData[x+1][y][otherInS] = 0;
          }
        }
      }
      otherOutS = getOtherOutConveyorSlot(x,y,x-1,y);
      if(otherOutS != -1){
        if(buildData[x-1][y][otherOutS+1] < IDConfigTxt[buildData[x-1][y][0]][0]){
          if(buildData[x][y][3] >= IDConfigTxt[buildData[x][y][0]][1]){
            buildData[x][y][3]-=IDConfigTxt[buildData[x][y][0]][1];
            buildData[x-1][y][otherOutS+1]+=IDConfigTxt[buildData[x][y][0]][1];
            if(buildData[x-1][y][otherOutS] == 0){
              buildData[x-1][y][otherOutS] = buildData[x][y][2];
            }
          }else{
            buildData[x-1][y][otherOutS+1]+=buildData[x][y][3];
            if(buildData[x-1][y][otherOutS] == 0){
              buildData[x-1][y][otherOutS] = buildData[x][y][2];
            }
            buildData[x][y][2] = 0;
            buildData[x][y][3] = 0;
          }
        }
      }
    }
  }
  public int getOtherInConveyorSlot(int x, int y, int x2, int y2){
    int s = 0;
    if(buildData[x2][y2][0] != 1){
      if(buildData[x2][y2][2] != 0 && buildData[x][y][2] == buildData[x2][y2][2]){
        s = 2;
      }else if(buildData[x2][y2][4] != 0 && buildData[x][y][2] == buildData[x2][y2][4]){
        s = 4;
      }else if(buildData[x2][y2][6] != 0 && buildData[x][y][2] == buildData[x2][y2][6]){
        s = 6;
      }else if(buildData[x][y][2] == 0){
        if(buildData[x2][y2][2] != 0){
          s = 2;
        }else if(buildData[x2][y2][4] != 0){
          s = 4;
        }else if(buildData[x2][y2][6] != 0){
          s = 6;
        }else{
          s = -1;
        }
      }else{
        s = -1;
      }
    }else{
      s = -1;
    }
    return s;
  }
  public int getOtherOutConveyorSlot(int x, int y, int x2, int y2){
    int s = 0; int s2 = IDConfigTxt[buildData[x2][y2][0]][2]; int fs = 0;
    if(s2 >= 1 && buildData[x2][y2][2] == 0){
      fs = 2;
    }else if(s2 >= 2 && buildData[x2][y2][4] == 0){
      fs = 4;
    }else if(s2 >= 3 && buildData[x2][y2][6] == 0){
      fs = 6;
    }else{
      fs = -1;
    }
    if(buildData[x][y][2] == buildData[x2][y2][2] && s2 >= 1){
      s = 2;
    }else if(buildData[x][y][2] == buildData[x2][y2][4] && s2 >= 2){
      s = 4;
    }else if(buildData[x][y][2] == buildData[x2][y2][6] && s2 >= 3){
      s = 6;
    }else if(fs != -1){
      s = fs;
    }else{
      s = -1;
    }
    return s;
  }
}
/*
FIXES
conveyor belts aändert ratation id und kopirt id bei pther input
conveyor belt bevorzugt lehre slots other out




*/


//---MainData[] Verteilung---
/*
mainData[0] = Skalirung (zoom)
mainData[1] = local --} berechnung zoom
mainData[2] = x Offset
mainData[3] = y Offset
mainData[4] = x frez mousex
mainData[5] = y frez mousey
mainData[6] = sperre überschreibung (build core)
mainData[7] =
mainData[8] = Select kadegorie bm
mainData[9] = Select Component bm
mainDta[10] = building rotation
*/

//---ColorData Verteilung---
/*
color 1 coal ore
color 2 mines
color 3 curser
color 4 conveyor
*/
//--array list --
/*
[][0] x pos
[][1] y pox
[][2] id
[][3] item id
[][4] item count
[][5] rotation
*/
  public void settings() {  size(600,600,P2D); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "FactoryV6" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
