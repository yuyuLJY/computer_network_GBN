//�ͻ��˶��߳� ����������
	class Client extends Thread{
		
		public void run(Sever s) {
			responseAck(s);
		}
		
		public void responseAck(Sever s) {
			//��ͻ��˵�getAck�������ͣ��Ѿ�ȷ�ϵ����ݶ�
			s.getAck(s.Id);
		}
		
	}
