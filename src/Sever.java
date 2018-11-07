//服务器端线程：服务器发数据
	//发送数据（做20次）
	//获取ACK，判断是否超时，超时重传
class Sever extends Thread{
		//!!!数据从1开始
	    public final int  SEND_WIND_SIZE = 4;//发送窗口大小为 10,如果将窗口大小设为 1，则为停-等协议 
	    public final int SEQ_SIZE = 20;//序列号的个数，从 0~19 共计 20 个 
		public int Id;
		public String data;
		GBN g = new GBN();
		String [] dataSet = new String[SEQ_SIZE+1];
		public Sever(int Id,String data) {
			this.Id = Id;
			this.data = data;
		}
		
		public void run() {
			//读取数据集dataSet
			//TODO 修补数据集
			for(int i=0;i<=20;i++) {//构造dataSet集合
				dataSet[i] = "data"+String.valueOf(i); 
				System.out.println(dataSet[i]);
			}
			boolean isAvailable = seqIsAvailable ();
			int count = 0;
			if(isAvailable) {//可用
				for(int i=1;i<=SEQ_SIZE;i++) {//发送数据
					if(i % (SEND_WIND_SIZE+1)==0) {//i = 5
						//需要等到本次轮回的，第一个ACK到了才可以再发送
						//比如，1 2 3 4发送完，第五个要等待，确定第1个客户的第一个ACK发回来了
						//TODO
						//睡眠直到 g.getcurSeq()> i -SEND_WIND_SIZE //当前的确认大于 5-4 = 1  9-4 = 5
						//TODO 发送第5个
						while(g.getcurSeq()< i -SEND_WIND_SIZE) {
							System.out.println("前边一轮数据尚未接收成功，不能发送");
							count++;
						}
						System.out.printf("等待_第%d个数据已发送\n",i);
						Sever s = new Sever(i,dataSet[i]);
						s.Id = i;
						s.data = dataSet[i];//第i段数据已经发送了
						Client client = new Client();
						client.run(s);//把两个线程都创建
					}else {
						System.out.printf("第%d个数据已发送\n",i);
						Sever s = new Sever(i,dataSet[i]);
						s.Id = i;
						s.data = dataSet[i];//第i段数据已经发送了
						Client client = new Client();
						client.run(s);//把两个线程都创建
					}
				}
			}else {//序列不可用
				System.out.println("序列不可用");
			}
		}
		
		/**
		 * 当前序列号 curSeq 是否可用 
		 * @return 有可用序列，返回True
		 */
		boolean seqIsAvailable () {
			if(g.getcurSeq() < SEQ_SIZE) {//当前可用的序列不满
				return true;
			}else {
				return false;
			}
			
		}
		

		/**
		 * 从客户端得到ACK反馈，并且把curSeq设置成反馈回来的ack
		 * @param ack
		 */
		void getAck(int ack) {
			System.out.printf("getAck ack:%d curSeq:%d  \n",ack,g.getcurSeq());
			if(ack == g.getcurSeq()+1) {//当前的ACK为期待的ACK
				g.setcurSeq(ack);
				System.out.printf("seq被设置成：%d\n",g.getcurSeq());
			}else {
				//TODO 并不是所需要的ACK，如何处理？设置计数器，超过几次就超时
				
				
			}
		}
		
		
	}
	