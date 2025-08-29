import { Outlet } from 'react-router-dom';
import Sidebar from './Sidebar';

const AppLayout = () => {
  return (
    <div className="min-h-screen bg-gray-900 text-white">
      <div className="flex">
        <Sidebar />
        <main className="flex-1 p-10">
          <Outlet /> 
        </main>
      </div>
    </div>
  );
};

export default AppLayout;