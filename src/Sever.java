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
		public Sever(int Id,String data) {
			this.Id = Id;
			this.data = data;
		}
		
		public void run() {
			//��ȡ���ݼ�dataSet
			//TODO �޲����ݼ�
			String [] dataSet = null;
			boolean isAvailable = seqIsAvailable ();
			if(isAvailable) {//����
				for(int i=1;i<SEQ_SIZE;i++) {//��������
					if(i % (SEND_WIND_SIZE+1)==0) {//i = 5
						//��Ҫ�ȵ������ֻصģ���һ��ACK���˲ſ����ٷ���
						//���磬1 2 3 4�����꣬�����Ҫ�ȴ���ȷ����1���ͻ��ĵ�һ��ACK��������
						//TODO
						//˯��ֱ�� g.getcurSeq()> i -SEND_WIND_SIZE //��ǰ��ȷ�ϴ��� 5-4 = 1  9-4 = 5
						//TODO ���͵�5��
					}else {
						Sever s = new Sever(i,dataSet[i]);
						s.Id = i;
						s.data = dataSet[i];//��i�������Ѿ�������
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
			if(ack == g.getcurSeq()+1) {//��ǰ��ACKΪ�ڴ���ACK
				g.setcurSeq(ack);
			}else {
				//TODO ����������Ҫ��ACK����δ������ü��������������ξͳ�ʱ
				
				
			}
		}
		
		
	}
	