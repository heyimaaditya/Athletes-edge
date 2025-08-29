interface StatCardProps {
  title: string;
  value: string | number;
  unit?: string;
}

const StatCard = ({ title, value, unit }: StatCardProps) => {
  return (
    <div className="bg-gray-800 p-6 rounded-lg shadow-lg">
      <h3 className="text-sm font-medium text-gray-400">{title}</h3>
      <p className="mt-2 text-3xl font-semibold text-white">
        {value} <span className="text-base font-normal text-gray-300">{unit}</span>
      </p>
    </div>
  );
};

export default StatCard;