import { LineChart, Line, XAxis, YAxis, CartesianGrid, Tooltip, Legend, ResponsiveContainer, ReferenceArea } from 'recharts';

interface AcwrDataPoint {
  date: string;
  acwr: number;
}

interface AcwrTrendChartProps {
  data: AcwrDataPoint[];
}

const AcwrTrendChart = ({ data }: AcwrTrendChartProps) => {
 
  const formattedData = data.map(item => ({
    ...item,
    // Format "YYYY-MM-DD" to "MMM DD"
    date: new Date(item.date).toLocaleDateString('en-US', { month: 'short', day: 'numeric' }),
   
    acwr: Math.round(item.acwr * 100) / 100
  }));

  return (
    <div className="bg-gray-800 p-6 rounded-lg mt-10">
      <h3 className="text-xl font-semibold mb-4">ACWR Trend (Last 60 Days)</h3>
      <ResponsiveContainer width="100%" height={400}>
        <LineChart data={formattedData}>
          <CartesianGrid strokeDasharray="3 3" stroke="#4A5568" />
          <XAxis dataKey="date" stroke="#A0AEC0" />
          <YAxis stroke="#A0AEC0" domain={[0, 2.5]}/>
          <Tooltip contentStyle={{ backgroundColor: '#2D3748', border: 'none' }}/>
          <Legend />

          {/* Danger Zone */}
          <ReferenceArea y1={1.5} y2={2.5} fill="#C53030" fillOpacity={0.2} label={{ value: "Danger Zone", position: "insideTopRight", fill: "#FED7D7" }} />
          {/* Safe Zone */}
          <ReferenceArea y1={0.8} y2={1.3} fill="#38A169" fillOpacity={0.2} label={{ value: "Safe Zone", position: "insideTopRight", fill: "#C6F6D5" }} />

          <Line type="monotone" dataKey="acwr" name="ACWR Ratio" stroke="#4299E1" strokeWidth={2} dot={{ r: 4 }} activeDot={{ r: 8 }} />
        </LineChart>
      </ResponsiveContainer>
    </div>
  );
};

export default AcwrTrendChart;