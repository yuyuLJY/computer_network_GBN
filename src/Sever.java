import java.util.ArrayList;
import java.util.Random;

//���������̣߳�������������
	//�������ݣ���20�Σ�
	//��ȡACK���ж��Ƿ�ʱ����ʱ�ش�
class Sever extends Thread{
		//!!!���ݴ�1��ʼ
	    public final int  SEND_WIND_SIZE = 4;//���ʹ��ڴ�СΪ 10,��������ڴ�С��Ϊ 1����Ϊͣ-��Э�� 
	    public final int SEQ_SIZE = 20;//���кŵĸ������� 0~19 ���� 20 �� 
		public int Id;
		public String data;
		GBN g = new GBN();
		String [] dataSet = new String[SEQ_SIZE+2];
		public long recordStartTime[] = new long[SEQ_SIZE+1];
		
		ArrayList<Integer> ackLossSet = new ArrayList<Integer>();//��Ӧack��ʧ���кű�
		public Sever(int Id,String data) {
			this.Id = Id;
			this.data = data;
		}
		
		public void run() {
			//��ȡ���ݼ�dataSet
			//�޲����ݼ�
			System.out.println("-------------------���data���Ķ��Ƿ�����----------");
			for(int i=0;i<=SEQ_SIZE;i++) {//����dataSet����
				dataSet[i] = "data"+String.valueOf(i); 
				System.out.println(dataSet[i]);
			}
			//����ack��sendData���嶪ʧ�εĺ���
			//TODO ������һ����֧datasendloss
			//ArrayList<Integer> dataLossSet = new ArrayList<Integer>();
			ackLossSet = createLoss("ack");
			System.out.println("-------------------���ack�����б��Ƿ�����----------");
			for(int i:ackLossSet) {
				System.out.printf("�����б�%d\n",i);
			}
			boolean isAvailable = seqIsAvailable ();
			int count = 0;
			
			
			if(isAvailable) {//����
				for(int i=1;i<=SEQ_SIZE;i++) {//��������
					if(i % (SEND_WIND_SIZE+1)==0) {//i = 5
						//��Ҫ�ȵ������ֻصģ���һ��ACK���˲ſ����ٷ���
						//���磬1 2 3 4�����꣬�����Ҫ�ȴ���ȷ����1���ͻ��ĵ�һ��ACK��������
						//TODO
						//˯��ֱ�� g.getcurSeq()> i -SEND_WIND_SIZE //��ǰ��ȷ�ϴ��� 5-4 = 1  9-4 = 5
						//TODO ���͵�5��
						
						while(g.getcurSeq()< i -SEND_WIND_SIZE) {
							System.out.println("Error1�����ܷ���");
							count++;
							if(isOutTime(recordStartTime[i])) {//��ʱ��
								System.out.printf("OutTime1��%d���³�ʱ\n",i);
								//�ط�����������е��ļ���1,2,3,4
								for(int j = i -SEND_WIND_SIZE;j< i;j++) {
									System.out.printf("OutTime1 �ָ��������ݶ� %d\n",j);
									long begin = System.currentTimeMillis();//��ʼ��ʱ
									recordStartTime[j] = begin;//���¼�¼ʱ��
									Sever s = new Sever(j,dataSet[j]);
									s.Id = j;
									s.data = dataSet[j];//��i�������Ѿ�������
									Client client = new Client(ackLossSet.contains(j));
									client.run(s);//�������̶߳�����
								}
							}
						}
					}
					long begin = System.currentTimeMillis();//��ʼ��ʱ
					recordStartTime[i] = begin;//��¼������̵߳���ʼʱ��
					Sever s = new Sever(i,dataSet[i]);
					s.Id = i;
					s.data = dataSet[i];//��i�������Ѿ�������
					System.out.printf("��%d�������ѷ���\n",i);
					Client client = new Client(ackLossSet.contains(i));
					client.run(s);//�������̶߳�����
					
				}
			}else {//���в�����
				System.out.println("���в�����");
			}
		}
		
		/**
		 * ��ǰ���к� curSeq �Ƿ���� 
		 * @return �п������У�����True
		 */
		boolean seqIsAvailable () {
			if(g.getcurSeq() < SEQ_SIZE) {//��ǰ���õ����в���
				return true;
			}else {
				return false;
			}
			
		}
		

		/**
		 * �ӿͻ��˵õ�ACK���������Ұ�curSeq���óɷ���������ack
		 * @param ack
		 */
		void getAck(int ack) {
			System.out.printf("getAck ack:%d curSeq:%d  \n",ack,g.getcurSeq());
			if(ack == g.getcurSeq()+1) {//��ǰ��ACKΪ�ڴ���ACK
				g.setcurSeq(ack);
				recordStartTime[ack] = 0;//�رն�ʱ
				System.out.printf("seq�����óɣ�%d\n",g.getcurSeq());
			}else if(ack > g.getcurSeq()+1 ){
				//TODO ����������Ҫ��ACK����δ������ü��������������ξͳ�ʱ
				System.out.println("Error2�������ۻ�");
				if(isOutTime(recordStartTime[g.getcurSeq()+1])) {//��ʱ��
					System.out.printf("OutTime2��%d���³�ʱ\n",g.getcurSeq()+1);
					//������֮��Ķ����ط�  
					int j = g.getcurSeq()+1;
					do {
					//TODO
					long begin = System.currentTimeMillis();//��ʼ��ʱ
					recordStartTime[j] = begin;//���¼�¼ʱ��
					System.out.printf("OutTime2 �ָ��������ݶ� %d\n",j);
					Sever s = new Sever(j,dataSet[j]);
					s.Id = j;
					s.data = dataSet[j];//��i�������Ѿ�������
					Client client = new Client(ackLossSet.contains(j));
					client.run(s);//�������̶߳�����
					System.out.printf("%d ",j);
					j++;
					}while(((j-1) % SEND_WIND_SIZE!=0) && j<=SEQ_SIZE);
				}
			}else {
				System.out.printf("�ظ�ack��Ӧ��%d,������\n",ack);
			}
		}
		
		/**
		 * ���ȷ����ʧ�����к�
		 * @param s
		 * @return
		 */
		ArrayList<Integer> createLoss(String s) {
			int lossNumber;
			int index;//Ԥ����������к�
			ArrayList<Integer> indexSet = new ArrayList<Integer>();
			if(s.equals("ack")) {//����ǲ���ack��ʧ������
				double AckLoss = g.getAckLoss();
				lossNumber = (int)(AckLoss * SEQ_SIZE);//ǿ�ưѶ�������ת��������
			}else {//����dataSend��ʧ������
				double dataLoss = g.getDataLoss();
				lossNumber = (int)(dataLoss * SEQ_SIZE);//ǿ�ưѶ�������ת��������
			}
			Random random = new Random();
			for(int i=0;i<lossNumber;i++) {
				index = random.nextInt(SEQ_SIZE-1)+1;//Ϊ�˷�ֹ����data0����Ϊǰ����裬��data1��ʼ
				indexSet.add(index);
			}
			return indexSet;
		}
		
		/**
		 * �ж��Ƿ�ʱ
		 * @param start
		 * @return
		 */
		boolean isOutTime(long start){
			long now = System.currentTimeMillis();
			long duration = now - start;
			if(duration>1000000) {//��ʱ
				return true;
			}
			return false; 
		}
		
	}
	