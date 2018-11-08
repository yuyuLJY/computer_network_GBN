import java.util.ArrayList;
import java.util.Random;

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
		String [] dataSet = new String[SEQ_SIZE+2];
		public long recordStartTime[] = new long[SEQ_SIZE+1];
		
		ArrayList<Integer> ackLossSet = new ArrayList<Integer>();//回应ack丢失序列号表
		public Sever(int Id,String data) {
			this.Id = Id;
			this.data = data;
		}
		
		public void run() {
			//读取数据集dataSet
			//修补数据集
			System.out.println("-------------------检查data报文段是否生成----------");
			for(int i=0;i<=SEQ_SIZE;i++) {//构造dataSet集合
				dataSet[i] = "data"+String.valueOf(i); 
				System.out.println(dataSet[i]);
			}
			//产生ack、sendData具体丢失段的函数
			//TODO 完善另一个分支datasendloss
			//ArrayList<Integer> dataLossSet = new ArrayList<Integer>();
			ackLossSet = createLoss("ack");
			System.out.println("-------------------检查ack错误列表是否生成----------");
			for(int i:ackLossSet) {
				System.out.printf("错误列表：%d\n",i);
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
							System.out.println("Error1：不能发送");
							count++;
							if(isOutTime(recordStartTime[i])) {//超时了
								System.out.printf("OutTime1：%d导致超时\n",i);
								//重发这个窗口所有的文件，1,2,3,4
								for(int j = i -SEND_WIND_SIZE;j< i;j++) {
									System.out.printf("OutTime1 恢复发送数据段 %d\n",j);
									long begin = System.currentTimeMillis();//开始计时
									recordStartTime[j] = begin;//重新记录时间
									Sever s = new Sever(j,dataSet[j]);
									s.Id = j;
									s.data = dataSet[j];//第i段数据已经发送了
									Client client = new Client(ackLossSet.contains(j));
									client.run(s);//把两个线程都创建
								}
							}
						}
					}
					long begin = System.currentTimeMillis();//开始计时
					recordStartTime[i] = begin;//记录下这个线程的起始时间
					Sever s = new Sever(i,dataSet[i]);
					s.Id = i;
					s.data = dataSet[i];//第i段数据已经发送了
					System.out.printf("第%d个数据已发送\n",i);
					Client client = new Client(ackLossSet.contains(i));
					client.run(s);//把两个线程都创建
					
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
				recordStartTime[ack] = 0;//关闭定时
				System.out.printf("seq被设置成：%d\n",g.getcurSeq());
			}else if(ack > g.getcurSeq()+1 ){
				//TODO 并不是所需要的ACK，如何处理？设置计数器，超过几次就超时
				System.out.println("Error2：不能累积");
				if(isOutTime(recordStartTime[g.getcurSeq()+1])) {//超时了
					System.out.printf("OutTime2：%d导致超时\n",g.getcurSeq()+1);
					//这个序号之后的都得重发  
					int j = g.getcurSeq()+1;
					do {
					//TODO
					long begin = System.currentTimeMillis();//开始计时
					recordStartTime[j] = begin;//重新记录时间
					System.out.printf("OutTime2 恢复发送数据段 %d\n",j);
					Sever s = new Sever(j,dataSet[j]);
					s.Id = j;
					s.data = dataSet[j];//第i段数据已经发送了
					Client client = new Client(ackLossSet.contains(j));
					client.run(s);//把两个线程都创建
					System.out.printf("%d ",j);
					j++;
					}while(((j-1) % SEND_WIND_SIZE!=0) && j<=SEQ_SIZE);
				}
			}else {
				System.out.printf("重复ack回应：%d,被丢弃\n",ack);
			}
		}
		
		/**
		 * 随机确定丢失的序列号
		 * @param s
		 * @return
		 */
		ArrayList<Integer> createLoss(String s) {
			int lossNumber;
			int index;//预测产生的序列号
			ArrayList<Integer> indexSet = new ArrayList<Integer>();
			if(s.equals("ack")) {//这个是产生ack丢失的序列
				double AckLoss = g.getAckLoss();
				lossNumber = (int)(AckLoss * SEQ_SIZE);//强制把丢包个数转换成整数
			}else {//计算dataSend丢失的序列
				double dataLoss = g.getDataLoss();
				lossNumber = (int)(dataLoss * SEQ_SIZE);//强制把丢包个数转换成整数
			}
			Random random = new Random();
			for(int i=0;i<lossNumber;i++) {
				index = random.nextInt(SEQ_SIZE-1)+1;//为了防止出现data0，因为前面假设，从data1开始
				indexSet.add(index);
			}
			return indexSet;
		}
		
		/**
		 * 判断是否超时
		 * @param start
		 * @return
		 */
		boolean isOutTime(long start){
			long now = System.currentTimeMillis();
			long duration = now - start;
			if(duration>1000000) {//超时
				return true;
			}
			return false; 
		}
		
	}
	