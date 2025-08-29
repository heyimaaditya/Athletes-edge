import { RiRobot2Line, RiUserLine } from 'react-icons/ri'; 

interface ChatMessageProps {
  message: { role: string; content: string };
}

const ChatMessage = ({ message }: ChatMessageProps) => {
  const isUser = message.role === 'user';
  return (
    <div className={`flex items-start gap-4 my-4 ${isUser ? 'justify-end' : ''}`}>
      {!isUser && (
        <div className="bg-indigo-600 rounded-full p-2">
          <RiRobot2Line size={20} />
        </div>
      )}
      <div className={`max-w-xl p-4 rounded-lg ${isUser ? 'bg-blue-600' : 'bg-gray-700'}`}>
        <p className="whitespace-pre-wrap">{message.content}</p>
      </div>
      {isUser && (
        <div className="bg-gray-600 rounded-full p-2">
          <RiUserLine size={20} />
        </div>
      )}
    </div>
  );
};

export default ChatMessage;