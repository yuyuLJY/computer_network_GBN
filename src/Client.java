import java.util.ArrayList;

//�ͻ��˶��߳� ����������
	class Client extends Thread{
		boolean lossFlag;//ÿ������Sever��ʱ�򶼸���Sever������Ƿ�ʧakc
		ArrayList<Integer> secondSent = new ArrayList<Integer>();//ǰ���Ѿ��ж�ʧ���ˣ��ڶ��η���ack�ˣ�����ʧ��
		public Client(boolean lossFlag) {
			this.lossFlag = lossFlag;
		}
		
		public void run(Sever s) {
			System.out.println("Client��������");
			responseAck(s);
			//TODO ������������
		}
		
		public void responseAck(Sever s) {
			if(s.Id==0) {
				
			} 
			else if(lossFlag == true ) {//ֻ�в���ʧ��־Ϊtrue��ʱ�򣬲ŷ���ack;����ֻ�赲һ�Σ��´ξͳɹ���
				if(secondSent.contains(s.Id)) {//ǰ���Ѿ���ʧһ����
					System.out.printf("���շ���Ӧack:%d\n ",s.Id);
					s.getAck(s.Id);
				}else {
					//��ͻ��˵�getAck�������ͣ��Ѿ�ȷ�ϵ����ݶ�
					System.out.printf("��ǰ���ֶΣ�%d,ack��ʧ\n",s.Id);
					secondSent.add(s.Id);//��¼�������ʧ�����ݺ�
				}
			}else {
				System.out.printf("���շ���Ӧack:%d\n ",s.Id);
				s.getAck(s.Id);
			}
		}
		
	}
