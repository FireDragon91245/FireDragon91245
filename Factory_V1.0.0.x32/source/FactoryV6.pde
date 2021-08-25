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
void setup(){
  frameRate(80);
  options[0] = true;
  options[1] = true;
  //size(displayWidth,displayHeight,P2D);
  size(600,600,P2D);
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
void draw(){
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
  mainData[4] = round(((mouseX - mainData[2]) / mainData[0])-0.5);
  mainData[5] = round(((mouseY - mainData[3]) / mainData[0])-0.5);
  mainData[7]++;
  if(mainData[7] >= 60){
    mainData[7] = 0;
    ih.mainItem();
  }
}
void keyPressed(){
  gh.statsMenuTogle();
  gh.reset();
  bh.rotate();
  bh.delete();
}
void mousePressed(){
  bh.build();
  bh.buildSelect();
}
