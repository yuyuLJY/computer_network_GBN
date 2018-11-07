//客户端端线程 ：接受数据
	class Client extends Thread{
		
		public void run(Sever s) {
			System.out.println("Client启动监听");
			responseAck(s);
			//TODO 丢包率如何设计
		}
		
		public void responseAck(Sever s) {
			//向客户端的getAck函数传送，已经确认的数据段
			s.getAck(s.Id);
		}
		
	}
