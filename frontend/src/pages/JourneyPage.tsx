import { useState, useEffect } from 'react';
import { Chrono } from 'react-chrono';
import { getMyProfile, getMyTimeline } from '../services/dataService';
import { RiTrophyLine, RiCrosshairLine, RiRunLine, RiFlagLine, RiMedalLine } from 'react-icons/ri';


interface TimelineEvent {
    title: string;
    cardTitle: string;
    cardSubtitle: string;
    cardDetailedText: string;
    date: string; 
    type: "PR" | "GOAL" | "WORKOUT" | "MILESTONE";
}

const JourneyPage = () => {
    const [items, setItems] = useState<any[]>([]); 
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        const fetchData = async () => {
            try {
               
                const profile = await getMyProfile();
                if (profile && profile.id) {
                    const timelineData: TimelineEvent[] = await getMyTimeline(profile.id);
                    
                   
                    const chronoItems = timelineData.map(event => ({
                        title: event.date, 
                        cardTitle: event.cardTitle,
                        cardSubtitle: event.cardSubtitle,
                        cardDetailedText: event.cardDetailedText,
                      
                        customContent: (
                            <div className="flex items-center gap-4 p-2">
                                <span className="text-3xl text-amber-400">{getIcon(event.type)}</span>
                                <div>
                                    <h3 className="font-bold">{event.cardTitle}</h3>
                                    <p className="text-sm text-gray-300">{event.cardSubtitle}</p>
                                </div>
                            </div>
                        )
                    }));
                    setItems(chronoItems);
                }
            } catch (error) {
                console.error("Failed to fetch journey timeline", error);
            } finally {
                setLoading(false);
            }
        };
        fetchData();
    }, []);

    
    const getIcon = (type: TimelineEvent['type']) => {
        switch(type) {
            case 'PR': return <RiTrophyLine />;
            case 'GOAL': return <RiCrosshairLine />;
            case 'MILESTONE': return <RiMedalLine />;
            case 'WORKOUT': return <RiRunLine />;
            default: return <RiFlagLine />;
        }
    };

    if (loading) {
        return <div className="text-center p-10">Constructing your epic journey...</div>;
    }

    return (
        <div>
            <h2 className="text-4xl font-bold mb-2">Your Athlete's Journey</h2>
            <p className="text-gray-400 mb-8">A timeline of your hard work, achievements, and milestones.</p>
            
            {items.length > 0 ? (
               
                <div style={{ width: '100%', height: '75vh' }}>
                    <Chrono
                        items={items}
                        mode="VERTICAL_ALTERNATING" 
                        cardHeight={100}
                        theme={{
                            primary: '#FBBF24', 
                            secondary: '#374151',
                            cardBgColor: '#1F2937', // gray-800 for card background
                            cardForeColor: 'white',
                            titleColor: 'white',
                            titleColorActive: '#FBBF24',
                        }}
                        fontSizes={{
                            cardSubtitle: '0.85rem',
                            cardText: '0.8rem',
                            cardTitle: '1rem',
                            title: '1rem',
                        }}
                       
                        scrollable={{ scrollbar: true }}
                    >
                       
                    </Chrono>
                </div>
            ) : (
                <div className="text-center bg-gray-800 p-8 rounded-lg mt-10">
                    <p className="text-gray-400">Your journey is yet to begin. Log your first workout to start your story!</p>
                </div>
            )}
        </div>
    );
};

export default JourneyPage;