import { Link } from 'react-router-dom';

const HomePage = () => {
  return (
    <div className="min-h-screen bg-gray-900 text-white flex flex-col items-center justify-center text-center p-4">
      <h1 className="text-5xl md:text-7xl font-bold mb-4">Athlete's Edge</h1>
      <p className="text-lg md:text-xl text-gray-300 mb-8 max-w-2xl">
        Track your workouts, monitor nutrition, and predict injury risks with our all-in-one platform.
        Take your performance to the next level.
      </p>
      <div>
        <Link to="/signup" className="bg-indigo-600 hover:bg-indigo-700 text-white font-bold py-3 px-6 rounded-lg text-lg mr-4">
          Get Started
        </Link>
        <Link to="/login" className="bg-gray-700 hover:bg-gray-600 text-white font-bold py-3 px-6 rounded-lg text-lg">
          Login
        </Link>
      </div>
    </div>
  );
};

export default HomePage;