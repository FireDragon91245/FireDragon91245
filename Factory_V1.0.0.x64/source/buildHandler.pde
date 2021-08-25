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
  void oreGen(){
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
  void build(){
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
  void buildSelect(){
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
  void rotate(){
    if(key == 'r'){
      if(mainData[10] == 4){
        mainData[10] = 1;
      }else{
        mainData[10]++;
      }
    }
  }
  void loadConfig(){
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
  void delete(){
    if(keyCode == 8){
      buildData[round(mainData[4])][round(mainData[5])][0] = 0;
      buildData[round(mainData[4])][round(mainData[5])][1] = 0;
      buildData[round(mainData[4])][round(mainData[5])][2] = 0;
      buildData[round(mainData[4])][round(mainData[5])][3] = 0;
    }
  }
}
