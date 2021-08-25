//Variables
int colors[][] = new int[20][8];
PFont fonts[] = new PFont[10];
Table loadColors;

//Main Graphic Class
class gHandler{
  color mainColor;
  PImage mainImage;
  String txt[] = {"","",""};
  //Render Ores
  void oreRender(){
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
  void zoom(){
     mainData[0] = 10 * (mainData[1] + 1);
     if(keyPressed){
       if(key == '+'){
         if(mainData[1] < 40){
           mainData[1] = mainData[1] + 0.1;
           println("Zoom: "+mainData[1]);
         }
       }
       if(key == '-'){
         if(mainData[1] > -0.9){
           mainData[1] = mainData[1] - 0.1;
           println("Zoom: "+mainData[1]);
         }
       }
     }
  }
  
  //Render grid (G)
  void gridRender(){
    if(key == 'g'){
      for(float i = mainData[3];i < (mainData[0] * (gridSize+1))+mainData[3]-1;i = i + mainData[0]){
        line(0+mainData[2],i,(mainData[0] * gridSize)+mainData[2],i);
      }
      for(float i = mainData[2];i < (mainData[0] * (gridSize+1))+mainData[2]-1;i = i + mainData[0]){
        line(i,0+mainData[3],i,(mainData[0] * gridSize)+mainData[3]);
      }
    }
  }
  
  void ofset(){
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
  void reset(){
     if(key == 'ü'){
       mainData[2] = 0;
       mainData[3] = 0;
       mainData[1] = 0;
     }
  }
  
  //Load color register
  void loadGraphicLibari(){
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
  void mousePreeLacation(){
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
  void imageRegister(){
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
  void bMenuRender(){
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
  void toolTips(){
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
  void statsMenuTogle(){
    if(keyCode == 99){
      if(options[2] == false){
        options[2] = true;
      }else{
        options[2] = false;
      }
    }
  }
  
  //printing stats menu
  void statsMenu(){
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
  void invRender(int id1, int count1, int id2, int count2, int id3, int count3){
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
