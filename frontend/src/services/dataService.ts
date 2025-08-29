import api from './api';



// --- USER PROFILE ---
export const getMyProfile = async () => {
  const response = await api.get('/users/me');
  return response.data;
};

// --- WORKOUTS ---
export const getMyWorkouts = async (athleteId: number) => {
  const response = await api.get(`/workouts/athlete/${athleteId}`);
  return response.data;
};

export const logWorkout = async (workoutData: any) => {
  const response = await api.post('/workouts', workoutData);
  return response.data;
};

// --- NUTRITION ---
export const getMyNutrition = async (athleteId: number) => {
  const response = await api.get(`/nutrition/athlete/${athleteId}`);
  return response.data;
}

// --- ANALYTICS & STATS ---
export const getMyStats = async (athleteId: number) => {
  const response = await api.get(`/stats/athlete/${athleteId}`);
  return response.data;
};

// --- INJURY PREDICTION / CHARTS ---
export const getAcwrHistory = async (athleteId: number) => {
  const response = await api.get(`/charts/acwr/${athleteId}?days=60`);
  return response.data;
};

// --- GOALS ---
export const getMyActiveGoals = async (athleteId: number) => {
  const response = await api.get(`/goals/athlete/${athleteId}`);
  return response.data;
};

export const createGoal = async (goalData: any) => {
  const response = await api.post('/goals', goalData);
  return response.data;
};

// --- PERSONAL RECORDS (PRs) ---
export const getMyPrs = async (athleteId: number) => {
  const response = await api.get(`/prs/athlete/${athleteId}`);
  return response.data;
};

export const logPr = async (prData: any) => {
  const response = await api.post('/prs', prData);
  return response.data;
};

// --- RECOVERY & READINESS ---
export const getTodaysReadiness = async (athleteId: number) => {
  const response = await api.get(`/recovery/athlete/${athleteId}/today`);
  return response.data;
};

export const logRecovery = async (recoveryData: any) => {
  const response = await api.post('/recovery', recoveryData);
  return response.data;
};

// --- WORKOUT PLANNER ---
export const getWorkoutTemplates = async () => {
  const response = await api.get('/planner/templates');
  return response.data;
};

export const getPlanForWeek = async (athleteId: number, weekStartDate: string) => {
  const response = await api.get(`/planner/plan/athlete/${athleteId}?weekStartDate=${weekStartDate}`);
  return response.data;
};

export const applyTemplate = async (data: any) => {
  return await api.post('/planner/plan/apply-template', data);
};


// --- AI COACH SERVICE ---
export const getTodaysCoachingTip = async (athleteId: number) => {
  const response = await api.get(`/coach/today/${athleteId}`);
  return response.data;
};

export const generateAiPlan = async (athleteId: number) => {
  return await api.post(`/coach/generate-plan/${athleteId}`);
};
export const getGamificationProfile = async (athleteId: number) => {
  const response = await api.get(`/gamification/profile/${athleteId}`);
  return response.data;
};


export const selectAvatar = async (athleteId: number, avatarType: string) => {
 
  const response = await api.post(`/gamification/profile/${athleteId}/select-avatar`, { avatarType });
  return response.data;
};


export const clearCoachCache = async (athleteId: number) => {
   
    await api.post(`/coach/clear-cache/${athleteId}`);
};

export const getMyTimeline = async (athleteId: number) => {
  const response = await api.get(`/timeline/athlete/${athleteId}`);
  return response.data;
};