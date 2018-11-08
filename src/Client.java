import java.util.ArrayList;

//客户端端线程 ：接受数据
	class Client extends Thread{
		boolean lossFlag;//每次启动Sever的时候都告诉Sever，这次是否丢失akc
		ArrayList<Integer> secondSent = new ArrayList<Integer>();//前面已经有丢失过了，第二次发送ack了，不丢失了
		public Client(boolean lossFlag) {
			this.lossFlag = lossFlag;
		}
		
		public void run(Sever s) {
			System.out.println("Client启动监听");
			responseAck(s);
			//TODO 丢包率如何设计
		}
		
		public void responseAck(Sever s) {
			if(s.Id==0) {
				
			} 
			else if(lossFlag == true ) {//只有不丢失标志为true的时候，才返回ack;但是只阻挡一次，下次就成功了
				if(secondSent.contains(s.Id)) {//前面已经丢失一次了
					System.out.printf("接收方响应ack:%d\n ",s.Id);
					s.getAck(s.Id);
				}else {
					//向客户端的getAck函数传送，已经确认的数据段
					System.out.printf("当前数字段：%d,ack丢失\n",s.Id);
					secondSent.add(s.Id);//记录下这个丢失的数据号
				}
			}else {
				System.out.printf("接收方响应ack:%d\n ",s.Id);
				s.getAck(s.Id);
			}
		}
		
	}
