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
		String [] dataSet = new String[SEQ_SIZE+1];
		public Sever(int Id,String data) {
			this.Id = Id;
			this.data = data;
		}
		
		public void run() {
			//��ȡ���ݼ�dataSet
			//TODO �޲����ݼ�
			for(int i=0;i<=20;i++) {//����dataSet����
				dataSet[i] = "data"+String.valueOf(i); 
				System.out.println(dataSet[i]);
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
							System.out.println("ǰ��һ��������δ���ճɹ������ܷ���");
							count++;
						}
						System.out.printf("�ȴ�_��%d�������ѷ���\n",i);
						Sever s = new Sever(i,dataSet[i]);
						s.Id = i;
						s.data = dataSet[i];//��i�������Ѿ�������
						Client client = new Client();
						client.run(s);//�������̶߳�����
					}else {
						System.out.printf("��%d�������ѷ���\n",i);
						Sever s = new Sever(i,dataSet[i]);
						s.Id = i;
						s.data = dataSet[i];//��i�������Ѿ�������
						Client client = new Client();
						client.run(s);//�������̶߳�����
					}
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
				System.out.printf("seq�����óɣ�%d\n",g.getcurSeq());
			}else {
				//TODO ����������Ҫ��ACK����δ������ü��������������ξͳ�ʱ
				
				
			}
		}
		
		
	}
	