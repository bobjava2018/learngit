package cn.baibo.tetris;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
 
/**
* 
* ��״��
*/
public abstract class Shape {
   Cell []cells=new Cell[4];
   private BufferedImage img;
/**
 * �����ƶ�
 */
public void right(){
	Cell[] cells=getCells();
	   for(Cell cell:cells){
		   cell.moveRight();
	   }
   }
 
/**
 * �����ƶ�
 */
public void left(){
	Cell[] cells=getCells();
	   for(Cell cell:cells){
		   cell.moveLeft();
	   }
   }
   public void drop(){
	   for(Cell cell:cells){
		   cell.drop();
	   }
   }
/**
 * ������ת
 * @param cells
 */
public void rollRight(Cell[] cells){
	   int row=cells[1].getRow();
	   int col=cells[1].getCol();
	   for(Cell cell:cells){
		   int r=cell.getRow();
		   int c=cell.getCol();
		   //��ͬһ�е����
		   if(r==row&&c!=col&&c!=col+2){
			   if(c==col-1){
				   cell.setRow(row-1);
			   }else{   
				   cell.setRow(row+1);
			   }
			   cell.setCol(col);
		   }else if(c==col&&r!=row){//��Բ�㲻ͬ�е��Ǻ�Բ��ͬ��
			   if(r==row-1){
				   cell.setCol(col+1);
			   }else{
				   cell.setCol(col-1);
			   }
			   cell.setRow(row);
		   }else if(r==row-1&&c!=col){//��Բ�㲻ͬ�в�ͬ�е���rowΪԲ���row-1
			   if(c==col-1){
				   cell.setRow(row-1);
			   }else{
				  
				   cell.setRow(row+1);
			   }
			   cell.setCol(col+1);
		   }else if(r==row+1&&c!=col){//��Բ�㲻ͬ�в�ͬ�е���rowΪԲ���row+1
			   if(c==col+1){
				   cell.setRow(row+1);
			   }else{
				   cell.setRow(row-1);
			   }
			   cell.setCol(col-1);
		   }else if(r==row&&c==col+2){//I�ĵ����һ������
			   cell.setCol(col);
			   cell.setRow(row+2);
		   }
	   }
	   
   }
/**
 * ������ת
 * @param cells
 */
public void rollLeft(Cell[] cells){
	   int row=cells[1].getRow();
	   int col=cells[1].getCol();
	   for(Cell cell:cells){
		   int r=cell.getRow();
		   int c=cell.getCol();
		   //��ͬһ�е����
		   if(r==row&&c!=col){
			   if(c==col-1){
				   cell.setRow(row+1);
			   }else{   
				   cell.setRow(row-1);
			   }
			   cell.setCol(col);
		   }else if(c==col&&r!=row&&r!=row+2){//��Բ�㲻ͬ�е��Ǻ�Բ��ͬ��
			   if(r==row-1){
				   cell.setCol(col-1);
			   }else{
				   cell.setCol(col+1);
			   }
			   cell.setRow(row);
		   }else if(r==row-1&&c!=col){//��Բ�㲻ͬ�в�ͬ�е���rowΪԲ���row-1
			   if(c==col-1){
				   cell.setRow(row+1);
			   }else{
				  
				   cell.setRow(row-1);
			   }
			   cell.setCol(col-1);
		   }else if(r==row+1&&c!=col){//��Բ�㲻ͬ�в�ͬ�е���rowΪԲ���row+1
			   if(c==col+1){
				   cell.setRow(row-1);
			   }else{
				   cell.setRow(row+1);
			   }
			   cell.setCol(col+1);
		   }else if(r==row+2&&c==col){
			   cell.setCol(col+2);
			   cell.setRow(row);
		   }
	   }
	   
   }
   public static BufferedImage getImage(String name){
	   try {
		   BufferedImage img=ImageIO.read(Shape.class.getResource(name));
		 	return img;
	} catch (IOException e) {
		e.printStackTrace();
		throw new RuntimeException();
	}
   }
   public abstract Cell[] getCells();
   public abstract void RightRoll();
   public abstract void LeftRoll();
}
class T extends Shape{
    private static BufferedImage img;
    static{
    	img=getImage("T.png");
    	
    }
	public T(){
		cells[0]=new Cell(0,4,img);
		cells[1]=new Cell(0,5,img);
		cells[2]=new Cell(0,6,img);
		cells[3]=new Cell(1,5,img);
	}
	public void RightRoll(){
		Cell[]ce=this.getCells();
		rollRight(ce);
	}
	public void LeftRoll(){
		Cell[]ce=this.getCells();
		rollLeft(ce);
	}
	@Override
	public Cell[] getCells() {
		return this.cells;
	}
}
	class L extends Shape{
	    private static BufferedImage img;
	    static{
	    	img=getImage("L.png");
	    	
	    }
		public L(){
			cells[0]=new Cell(1,4,img);
			cells[1]=new Cell(1,5,img);
			cells[2]=new Cell(1,6,img);
			cells[3]=new Cell(0,6,img);
		}
		public void RightRoll(){
			Cell[]ce=this.getCells();
			rollRight(ce);
		}
		public void LeftRoll(){
			Cell[]ce=this.getCells();
			rollLeft(ce);
		}
		@Override
		public Cell[] getCells() {
			return this.cells;
		}
	  
}
	class J extends Shape{
		private static BufferedImage img;
		static{
			img=getImage("L.png");
			
		}
		public J(){
			cells[0]=new Cell(1,4,img);
			cells[1]=new Cell(1,5,img);
			cells[2]=new Cell(1,6,img);
			cells[3]=new Cell(0,4,img);
		}
		public void RightRoll(){
			Cell[]ce=this.getCells();
			rollRight(ce);
		}
		public void LeftRoll(){
			Cell[]ce=this.getCells();
			rollLeft(ce);
		}
		@Override
		public Cell[] getCells() {
			return this.cells;
		}
		
	}
	class Z extends Shape{
		private static BufferedImage img;
		static{
			img=getImage("Z.png");
			
		}
		public Z(){
			cells[0]=new Cell(0,4,img);
			cells[1]=new Cell(1,5,img);
			cells[2]=new Cell(0,5,img);
			cells[3]=new Cell(1,6,img);
		}
		public void RightRoll(){
			Cell[]ce=this.getCells();
			rollRight(ce);
		}
		public void LeftRoll(){
			Cell[]ce=this.getCells();
			rollLeft(ce);
		}
		@Override
		public Cell[] getCells() {
			return this.cells;
		}
		
	}
	class S extends Shape{
		private static BufferedImage img;
		static{
			img=getImage("Z.png");
			
		}
		public S(){
			cells[0]=new Cell(1,4,img);
			cells[1]=new Cell(1,5,img);
			cells[2]=new Cell(0,5,img);
			cells[3]=new Cell(0,6,img);
		}
		public void RightRoll(){
			Cell[]ce=this.getCells();
			rollRight(ce);
		}
		public void LeftRoll(){
			Cell[]ce=this.getCells();
			rollLeft(ce);
		}
		@Override
		public Cell[] getCells() {
			return this.cells;
		}
		
	}
	class O extends Shape{
		private static BufferedImage img;
		static{
			img=getImage("O.png");
			
		}
		public O(){
			cells[0]=new Cell(1,4,img);
			cells[1]=new Cell(1,5,img);
			cells[2]=new Cell(0,4,img);
			cells[3]=new Cell(0,5,img);
		}
		public void RightRoll(){
		
		}
		public void LeftRoll(){	
		}
		@Override
		public Cell[] getCells() {
			return this.cells;
		}
		
	}
	class I extends Shape{
		private static BufferedImage img;
		static{
			img=getImage("I.png");
			
		}
		private int flag=0;
		public I(){
			cells[0]=new Cell(1,4,img);
			cells[1]=new Cell(1,5,img);
			cells[2]=new Cell(1,6,img);
			cells[3]=new Cell(1,7,img);
		}
		public void RightRoll(){//Iֻ��������״̬
			Cell[]ce=this.getCells();
			 if(flag++%2==0){
				 rollRight(ce); 
			 }else{
				 rollLeft(ce);
			 }
			
		}
		public void LeftRoll(){
			Cell[]ce=this.getCells();
			rollLeft(ce);
		}
		@Override
		public Cell[] getCells() {
			return this.cells;
		}
		
	}
