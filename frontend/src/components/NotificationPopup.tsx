interface NotificationPopupProps {
  message: string;
  onClose: () => void;
}

const NotificationPopup = ({ message, onClose }: NotificationPopupProps) => {
  return (
    <div className="fixed top-5 right-5 bg-red-600 text-white p-4 rounded-lg shadow-lg z-50 flex items-center animate-fade-in-down">
      <span className="mr-3 text-2xl">⚠️</span>
      <p>{message}</p>
      <button onClick={onClose} className="ml-4 text-white font-bold">&times;</button>
    </div>
  );
};

export default NotificationPopup;