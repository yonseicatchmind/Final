package application;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

public class SampleController implements Initializable {
	
	// variables 
	Socket socket;
    //TextArea textArea;
    
	 public GraphicsContext gcb, gcf; // canvas에 색 출력  gcf-canvas gcb-canvasef 
	 public boolean freedesign = true, erase = false, drawline = false,
			 drawoval = false,drawrectangle = false; //true false로 키고 끄기
	 double startX=0, startY=0, lastX=0,lastY=0,oldX=0,oldY=0,holdX=0,holdY=0;
	 double hg;
	 int strokeNow=5;
	 String Ids="",Chats="";
	 String IPs="127.0.0.1", Ports="1010";
	 int Port1=1010;
	 int ChatStart=0;
	 double rX1, rY1,  rX2, rY2;
	 int colorNow;
	 Paint colors=Color.rgb(0,0,0),rcolors=Color.rgb(0,0,0);
	 String colorS=colors.toString();
	 double sliders=5,rsliders;
	//Color.rgb(244,244,244); // 그림판 배경 색 
	 int firstConnect=0;
	 int score = 0;
	 String Msg,temp;
	 String fills="FillNo",rfills="FillNo";
	 int playerNum=0;
	 List<String> list = new ArrayList<String>();
	 String Id1;
	 class Player {

			String players[];
			int scores[];

		}
	 
	 Player p = new Player(); 
	 // FXML
	
	@FXML 
	 public TextField Answer,Talk;
	
	 public Canvas canvas, canvasef;
	 public Button Login,Send, Pencil;
	 public Button EndConnect,Clear,Eraser;
	 public Button oval,line,rect,confirm;
	 public ColorPicker colorpick;
	 public RadioButton strokeRB,fillRB;
	 public Slider sizeSlider;
	 public TextArea TalkBoard,Players;
	 
	// public ListView PlayerList;
	 public TextField ID,IP,Port;
	 public String ans="";
	@FXML
		public void onMousePressedListener(MouseEvent e){ //직선 및 도형 그릴 때 시작 끝 저장 
			this.startX = e.getX();
			this.startY = e.getY();
	
		}
	 @FXML
	    public void onMouseDraggedListener(MouseEvent e){ // 마우스 움직임 저장
	        this.lastX = e.getX();
	        this.lastY = e.getY();
	        	// 드래그 할 때 함수들 호출 및 알고리즘 
	     if (Ids.equals("teacher")) {
	        if(drawrectangle)
                drawRectEffect();
	        if(drawoval)
	            drawOvalEffect();
	        if(drawline)
	            drawLineEffect();
	        if(freedesign)
	   	        sendPensil();
	        if(erase)
	        	sendErase();
	     }
	    }
	  @FXML 
	    public void onMouseReleaseListener(MouseEvent e){ 
		 
		  if (Ids.equals("teacher")) {
	        if(drawrectangle)
	            sendRect();
	        if(drawoval)
	            sendOval();
	        if(drawline)
	            sendLine();
	        if(erase) ;
	        	//sendErase();
		  } 
	    }
	  @FXML 
	    public void onMouseEnteredListener(MouseEvent e){
		  this.holdX = e.getX();
		  this.holdY = e.getY();
//		  if(erase) {
//			  eraseEffect();
//		  }
	  
	  }
	  
	
	  
	  @FXML
	    public void onMouseExitedListener(MouseEvent event)
	    { //실험
//	        System.out.println("mouse exited");
	    }
	  
	  // draw method
	
	  public void sendPensil() // 마우스 이용 그리기  메소드 
	    {
		  if (Ids.equals("teacher")) {
	        gcb.setStroke(colorpick.getValue());
	        colors=colorpick.getValue();
	        colorS=colors.toString();
	        sliders=sizeSlider.getValue();
	        //sliders=5;
	       
	      	send("Pencil:" + startX + "," + startY+ "," +  lastX + "," + lastY+ "," + sliders+ "," +colorS +"," +fills);
	        
	      	startX=lastX;
	      	startY=lastY;
		  }       
	    }
	  
	   
	    private void sendRect() //사각형 그리는 메소드 
	    {
	    	 if (Ids.equals("teacher")) { 
	            gcb.setStroke(colorpick.getValue());
	            colors=colorpick.getValue();
	            colorS=colors.toString();
	            sliders=sizeSlider.getValue();
	            
		      	send("Rect:" + startX + "," + startY+ "," + lastX + "," + lastY+ "," + sliders+ "," +colorS+"," +fills);
	        } 
      
	    }

	    private void sendLine() //선 그리는 메소드 
	    {	
	    	 if (Ids.equals("teacher")) {
	    	
	        	gcb.setStroke(colorpick.getValue());
	            colors=colorpick.getValue();
	            colorS=colors.toString();
	            sliders=sizeSlider.getValue();
	            
		      	send("Line:" + startX + "," + startY+ "," + lastX + "," + lastY+ "," + sliders+ "," +colorS+"," +fills);
	        } 
	    }
	  
	
	  	private void sendOval() //타원 그리는 메소드 
	    {
	  		 if (Ids.equals("teacher")) {
	            gcb.setStroke(colorpick.getValue());
	            colors=colorpick.getValue();
	            colorS=colors.toString();
	            sliders=sizeSlider.getValue();
	            
	            send("Oval:" + startX + "," + startY+ "," + lastX + "," + lastY+ "," + sliders+ "," +colorS+"," +fills);
	        } 
	    }

	  	
	  	 private void sendErase() { 
	  		 if (Ids.equals("teacher")) { 
	  		    colors=Color.rgb(255,255,255);
	  		    colorS=colors.toString();
	  		    sliders=sizeSlider.getValue();
	      
	        	send("Erase:" + startX + "," + startY+ "," +  lastX + "," + lastY+ "," + sliders+ "," +colorS+"," +fills);
	        	
	        	startX=lastX;
		      	startY=lastY;
	        	
	        }    
	  	 }
	  	 
	  	 public void drawPencil() // 마우스 이용 그리기  메소드 
		    {
	  		    
	         	gcb.setStroke(rcolors);
		    	gcb.setLineWidth(rsliders);
		        gcb.strokeLine(rX1, rY1, rX2, rY2);
		
	
		    } 
	  	 public void drawErase() // 마우스 이용 그리기  메소드 
		    {
		    	
	         	gcb.setStroke(rcolors);
		    	gcb.setLineWidth(rsliders);
		        gcb.strokeLine(rX1, rY1, rX2, rY2);
		
	
		    } 
	    private void drawRect() //사각형 그리는 메소드 
	    {
	        double wh = rX2 - rX1;
	        double hg = rY2 - rY1;
	        
	        gcb.setStroke(rcolors);
	    	gcb.setLineWidth(rsliders);
	    	 	
	    	
	        if(rfills.equals("FillYes")){
	        	gcb.setFill(rcolors);   
	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcb.fillRect(rX1, rY1, wh, hg);
	        }else{
	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcb.strokeRect(rX1, rY1, wh, hg);
	            
	            
	        }
	    }

	    private void drawLine() //선 그리는 메소드 
	    {	
	    	    gcb.setStroke(colorpick.getValue());
	    		gcb.setStroke(rcolors);
		    	gcb.setLineWidth(rsliders);	
	    	    gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcb.strokeLine(rX1,rY1, rX2, rY2);
	    	
	    }
	    
	    private void drawOval() //타원 그리는 메소드 
	    {
	    	double wh = rX2 - rX1;
	        double hg = rY2 - rY1;
	        gcb.setStroke(rcolors);
	    	gcb.setLineWidth(rsliders);

	        if(rfills.equals("FillYes")){
	        	gcb.setFill(rcolors);   
	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcb.fillOval(rX1, rY1, wh, hg);
	        }else{
	        	gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcb.strokeOval(rX1, rY1, wh, hg);
	        }
	    }

	    
	   
	    private void clearsCanvas()
	    {
	        gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	        gcb.clearRect(0, 0, canvasef.getWidth(), canvasef.getHeight());
	    }
	    
	     // 도형 그릴 때 효과 
	    
	    
	    private void drawOvalEffect()
	    {
	        double wh = lastX - startX;
	        double hg = lastY - startY;
	        gcf.setLineWidth(sizeSlider.getValue());

	        if(fillRB.isSelected()){
	            gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcf.setFill(colorpick.getValue());
	            gcf.fillOval(startX, startY, wh, hg);
	          
	          
	        }else{
	            gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcf.setStroke(colorpick.getValue());
	            gcf.strokeOval(startX, startY, wh, hg );
	     
	          
	        }
	       }

	    private void drawRectEffect()
	    {
	        double wh = lastX - startX;
	        double hg = lastY - startY;
	        gcf.setLineWidth(sizeSlider.getValue());

	        if(fillRB.isSelected()){
	            gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcf.setFill(colorpick.getValue());
	            gcf.fillRect(startX, startY, wh, hg);
	          
	        }else{
	            gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	            gcf.setStroke(colorpick.getValue());
	            gcf.strokeRect(startX, startY, wh, hg );
	          
	        
	        }
	    }
	    
	    
	    private void drawLineEffect()
	    {
	        gcf.setLineWidth(sizeSlider.getValue());
	        gcf.setStroke(colorpick.getValue());
	        gcf.clearRect(0, 0, canvas.getWidth() , canvas.getHeight());
	        gcf.strokeLine(startX, startY, lastX, lastY);
	     
	     
	    }  
	 
	    @FXML
	    private void setColorChange(ActionEvent e)
	    {
	    	gcb.setStroke(colorpick.getValue());
	    	colors=colorpick.getValue();
	    	colorS=colors.toString();
	   	    	
	    }
	    
	    @FXML
	    private void setSliderChange(ActionEvent e)
	    {
	    	
	    	gcb.setLineWidth(sizeSlider.getValue());
	    	sliders=sizeSlider.getValue();
	    	//TalkBoard.appendText(String.valueOf(sliders));
	    
	    	
	    }
	    
	    @FXML
	    private void fillChanged(ActionEvent e)
	    {
	    	if (fillRB.isSelected() == true) {
	    		fills="FillYes";
	    	} else {
	    		fills="FillNo";
	    	}
	    	
	    
	    	
	    }
	
	    
	    @FXML
	    private void setOvalAsCurrentShape(ActionEvent e)
	    {
	        drawline = false;
	        drawoval = true;
	        drawrectangle = false;
	        freedesign = false;
	        erase = false;
	        gcb.setStroke(colorpick.getValue());
	    }

	     @FXML
	    private void setLineAsCurrentShape(ActionEvent e)
	    {
	        drawline = true;
	        drawoval = false;
	        drawrectangle = false;
	        freedesign = false;
	        erase = false;
	        gcb.setStroke(colorpick.getValue());
	    }
	     @FXML
	    private void setRectangleAsCurrentShape(ActionEvent e)
	    {
	        drawline = false;
	        drawoval = false;
	        freedesign = false;
	        erase=false;
	        drawrectangle = true;
	        gcb.setStroke(colorpick.getValue());
	    }
	     	 
	    @FXML 
	    private void clearCanvas(ActionEvent e)
	    {
	        //gcf.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
	        //gcb.clearRect(0, 0, canvasef.getWidth(), canvasef.getHeight());
	    	send("Clear:");
	    	//gcb.setStroke(colorpick.getValue());
	    }
	    
	    @FXML
	    public void setErase(ActionEvent e)
	    {
	        drawline = false;
	        drawoval = false;
	        drawrectangle = false;    
	        erase = true;
	        freedesign= false;
	       // gcb.setStroke(colorpick.getValue());
	    }

	    @FXML
	    public void setFreeDesign(ActionEvent e)
	    {
	        drawline = false;
	        drawoval = false;
	        drawrectangle = false;    
	        erase = false;
	        freedesign = true;
	        gcb.setStroke(colorpick.getValue());
	        
	    }
    
		
		
		
		
		
		@FXML 
	    private void inputID(ActionEvent e)
	    {
				 
	    }
		
		@FXML 
		 private void Logins(ActionEvent e)
		 {
			
			startClient(IPs, Port1); 
			
			Ids = ID.getText();
			
			Login.setDisable(true);
            Send.setDisable(false);
            Talk.setDisable(false);
            EndConnect.setDisable(false);
            Talk.requestFocus();
        
            if (Ids.equals("teacher")) { 
            	Pencil.setVisible(true);
            	oval.setVisible(true);
            	line.setVisible(true);
            	rect.setVisible(true);
            	Clear.setVisible(true);
            	Eraser.setVisible(true);
            	colorpick.setVisible(true);
            	sizeSlider.setVisible(true);
            	//confirm.setVisible(true);
            	fillRB.setVisible(true);
            }
            
               TalkBoard.appendText("<알림> "+ Ids +" 님이  입장하였습니다!!\n");
              // send("Connect:" + Ids+ " \n");	
	           Talk.requestFocus();
			       
		 } 
		 
	
		 @FXML 
		 private void InputChat(ActionEvent e)
		 {
			 
			   //Players.setText(null);
			   Chats = Talk.getText();
				
			   send("["+Ids+"] "+Chats+"\n");
			
			   Talk.setText("");
	           Talk.requestFocus();
	           //send("Connect:  " + Ids+ " \n");	
			       
		 } 
		 
		 @FXML 
		 private void refreshList(ActionEvent e)
		 {
			 
			  // Players.setText(null);
			             
	           send("Connect:" + Ids);	
	           Talk.requestFocus();    
	           
		 } 
		 
		 
		 @FXML 
		 private void endConnect(ActionEvent e)
		 {
				
			TalkBoard.appendText("<알림> "+ Ids +" 님이  퇴장하였습니다!!\n");
			Talk.setText("");
			
			stopClient() ;
	
            
            Login.setDisable(false);
            Send.setDisable(true);
			Talk.setDisable(true);
			EndConnect.setDisable(true);
		 } 
		 
		 @Override
			public void initialize(URL url, ResourceBundle rb) {
				// TODO Auto-generated method stub
				gcf = canvas.getGraphicsContext2D();
				gcb = canvasef.getGraphicsContext2D();
				
				Login.setDisable(false);
	            Send.setDisable(true);
	            Talk.setDisable(true);
	            TalkBoard.setEditable(false);
	 		    Players.setEditable(false); 
	 		    EndConnect.setDisable(true);
	 		    sizeSlider.setVisible(false);
	 		    fillRB.setVisible(false);
	 		    Pencil.setVisible(false);
             	oval.setVisible(false);
              	line.setVisible(false);
             	rect.setVisible(false);
             	Clear.setVisible(false);
             	Eraser.setVisible(false);
             	colorpick.setVisible(false);
             	//confirm.setVisible(false);
	 
             	colorpick.setValue(Color.rgb(0,0,0));
	             
		        ID.requestFocus();
		     
		        
			}	
		 
		 public void receiveMsg() {
			 
			 //String parts[];
				String[] pars = Msg.split(":");
				
				if (pars[0].equals("Pencil")) {
					pars[1].split(",");
					
					rX1 = Double.parseDouble(pars[1].split(",")[0]);
					rY1 = Double.parseDouble(pars[1].split(",")[1]);
					rX2 = Double.parseDouble(pars[1].split(",")[2]);
					rY2 = Double.parseDouble(pars[1].split(",")[3]);
					rsliders=Double.parseDouble(pars[1].split(",")[4]);
					rcolors= Color.web(pars[1].split(",")[5]);
						
					drawPencil();
					
				} else if (pars[0].equals("Erase")) {
						pars[1].split(",");
						
						rX1 = Double.parseDouble(pars[1].split(",")[0]);
						rY1 = Double.parseDouble(pars[1].split(",")[1]);
						rX2 = Double.parseDouble(pars[1].split(",")[2]);
						rY2 = Double.parseDouble(pars[1].split(",")[3]);
						rsliders=Double.parseDouble(pars[1].split(",")[4]);
						rcolors= Color.web(pars[1].split(",")[5]);
							
						drawErase();
				 
				} else if (pars[0].equals("Line")) {
					rX1 = Double.parseDouble(pars[1].split(",")[0]);
					rY1 = Double.parseDouble(pars[1].split(",")[1]);
					rX2 = Double.parseDouble(pars[1].split(",")[2]);
					rY2 = Double.parseDouble(pars[1].split(",")[3]);
					rsliders=Double.parseDouble(pars[1].split(",")[4]);
					rcolors= Color.web(pars[1].split(",")[5]);
					
					drawLine();
					
				} else if (pars[0].equals("Oval")) {
					rX1 = Double.parseDouble(pars[1].split(",")[0]);
					rY1 = Double.parseDouble(pars[1].split(",")[1]);
					rX2 = Double.parseDouble(pars[1].split(",")[2]);
					rY2 = Double.parseDouble(pars[1].split(",")[3]);
					rsliders=Double.parseDouble(pars[1].split(",")[4]);
					rcolors= Color.web(pars[1].split(",")[5]);
					rfills = pars[1].split(",")[6];
					drawOval();
					
				} else if (pars[0].equals("Rect")) {
					rX1 = Double.parseDouble(pars[1].split(",")[0]);
					rY1 = Double.parseDouble(pars[1].split(",")[1]);
					rX2 = Double.parseDouble(pars[1].split(",")[2]);
					rY2 = Double.parseDouble(pars[1].split(",")[3]);
					rsliders=Double.parseDouble(pars[1].split(",")[4]);
					rcolors= Color.web(pars[1].split(",")[5]);
					rfills = pars[1].split(",")[6];
					
					drawRect();
					
				} else if (pars[0].equals("Clear")) {
	
					clearsCanvas();
					
				} else if (pars[0].equals("Connect")) {
					Players.setText(null);
					playerNum=0;
					send("Member:" + Ids);	
					
					
				} else if (pars[0].equals("EndConnect")) {
					//clearsCanvas();
				} 
				else if (pars[0].equals("Member")) {
					
					//p.players[playerNum] =pars[1];
					//p.scores[playerNum] = 0;
					//TalkBoard.appendText(playerNum+p.players[playerNum] +p.scores[playerNum]+"\n");
					
			    	// Players.appendText(p.players[playerNum]+"   " +p.scores[playerNum]  +"\n");
			    	 Players.appendText(pars[1] +"\n");
					// playerNum++;
				} 
		 }
		 
		 
		 public void startClient(String IP, int port) {
		        Thread thread = new Thread() {
		            public void run() {
		                try {
		                    socket = new Socket(IP, port);
		                    //send("Connect: "+ Ids +" 님이  입장 하였습니다!!\n");
		                    receive();
		                    //send("");
		                    TalkBoard.appendText("[서버 접속 성공]\n");
		                } catch (Exception e) {
		                    // TODO: handle exception
		                    if (!socket.isClosed()) {    
		                        stopClient();
		                        TalkBoard.appendText("[서버 접속 실패]\n");
		                        Platform.exit();// 프로그램 종료
		                    }
		                }
		            }
		        };
		        thread.start();
		        
		    }
		 
		    // 클라이언트 종료 메소드
		    public void stopClient() {
		        try {
		            if (socket != null && !socket.isClosed()) {
		                socket.close();
		            }
		        } catch (Exception e) {
		            e.printStackTrace();
		        }
		    }
		 
		    // 서버로부터 메세지를 전달받는 메소드
		    public void receive() {
		        while (true) {
		            try {
		                InputStream in = socket.getInputStream();
		                byte[] buffer = new byte[512];
		                int length = in.read(buffer);
		                if (length == -1)
		                    throw new IOException();
		                String message = new String(buffer, 0, length, "UTF-8");
		                
		                
		                
		                if (message.contains(":")) {
		                	Msg=message;
		                	receiveMsg();
		                	
		                	
						}else {
		                	   Platform.runLater(() -> {
		                		   temp = message.substring(message.indexOf("]")+2,message.length());
		                		   temp = temp.trim();
		                		   if(message.contains("/q")) { 
		                			   TalkBoard.appendText("**********"+message.substring((message.indexOf("[")+1), message.indexOf("]")) + "가 문제를 출제하였습니다.**********\n");
		                			   ans=message.substring(message.lastIndexOf("q")+1, message.length()-1);
		                		   }
		                		   else if(message.contains("Score_reset_kk"))
		                			   score=0;
		                		   else
		                			   TalkBoard.appendText(message);
		                		   if(ans.equals(temp)) {
		                			   ans="";
		                			   list.add(message.substring(1, message.indexOf("]")));
		                			   while(true) {
		                				   if(list.contains(Ids)) {
			                				   score++;
			                				   list.remove(Ids);
			                			   }
		                				   else
		                					   break;
		                			   }
		                			   TalkBoard.appendText("**********"+message.substring((message.indexOf("[")+1), message.indexOf("]")) + "(이)가 정답을 맞췄습니다.**********[당신의 현재 점수]"+score+"\n");
		                			   if(score>=3) {
		                				   send("**********"+message.substring((message.indexOf("[")+1), message.indexOf("]")) + "의 승리**********\n");
		                				   list.clear();
		                				   send("**********점수를 초기화합니다**********\n");
		                				   send("Score_reset_kk");
		                			   }
		                		   }
		                       });
		                }
		          
		            } catch (Exception e) {
		                // TODO: handle exception
		                stopClient();
		                break;
		            }
		        }
		    }
		 
		    // 서버로 메세지를 보내는 메소드
		    public void send(String message) {
		        Thread thread1 = new Thread() {
		            public void run() {
		                try {
		                    OutputStream out = socket.getOutputStream();
		                    byte[] buffer = message.getBytes("UTF-8");
		                    out.write(buffer);
		                    out.flush();
		                } catch (Exception e) {
		                    // TODO: handle exception
		                    stopClient();
		                }
		            }
		        };
		        thread1.start();
		    }		 
		    // 동작 메소드 
}