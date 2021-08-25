class iHandler{
  void inventar(){
    if(mainData[4] >= 0 && mainData[4] < gridSize && mainData[5] >= 0 && mainData[5] < gridSize){
      //println(buildData[round(mainData[4])][round(mainData[5])][0],buildData[round(mainData[4])][round(mainData[5])][1],buildData[round(mainData[4])][round(mainData[5])][2],buildData[round(mainData[4])][round(mainData[5])][3],buildData[round(mainData[4])][round(mainData[5])][4],buildData[round(mainData[4])][round(mainData[5])][5],buildData[round(mainData[4])][round(mainData[5])][6],buildData[round(mainData[4])][round(mainData[5])][7]);
      gh.invRender(buildData[round(mainData[4])][round(mainData[5])][2],buildData[round(mainData[4])][round(mainData[5])][3],buildData[round(mainData[4])][round(mainData[5])][4],buildData[round(mainData[4])][round(mainData[5])][5],buildData[round(mainData[4])][round(mainData[5])][6],buildData[round(mainData[4])][round(mainData[5])][7]);
    }
  }
  void mainItem(){
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
  int getMineSlot(int x, int y, int x2, int y2){
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
  int getOtherInConveyorSlot(int x, int y, int x2, int y2){
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
  int getOtherOutConveyorSlot(int x, int y, int x2, int y2){
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
