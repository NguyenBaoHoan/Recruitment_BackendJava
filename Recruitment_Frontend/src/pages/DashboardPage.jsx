
// import { useAuth } from '../context/AuthContext';
// import { useNavigate } from 'react-router-dom';
// import { LogOut, User, Briefcase, Heart } from 'lucide-react';

// const DashboardPage = () => {
//   const { user, logout } = useAuth();
//   const navigate = useNavigate();

//   const handleLogout = async () => {
//     await logout();
//     navigate('/login');
//   };

//   return (
//     <div className="min-h-screen bg-gray-50">
//       {/* Header */}
//       <header className="bg-white shadow">
//         <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-4 flex justify-between items-center">
//           <h1 className="text-2xl font-bold text-gray-900">JobHunter Dashboard</h1>
//           <button
//             onClick={handleLogout}
//             className="flex items-center gap-2 px-4 py-2 bg-red-600 text-white rounded-lg hover:bg-red-700 transition-colors"
//           >
//             <LogOut size={20} />
//             Logout
//           </button>
//         </div>
//       </header>

//       {/* Main Content */}
//       <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
//         {/* Welcome Section */}
//         <div className="bg-white rounded-lg shadow p-6 mb-8">
//           <h2 className="text-2xl font-bold text-gray-900 mb-2">
//             Welcome back, {user?.name || 'User'}!
//           </h2>
//           <p className="text-gray-600">{user?.email}</p>
//         </div>

//         {/* Stats Grid */}
//         <div className="grid grid-cols-1 md:grid-cols-3 gap-6 mb-8">
//           <div className="bg-white rounded-lg shadow p-6">
//             <div className="flex items-center justify-between">
//               <div>
//                 <p className="text-sm text-gray-600">Profile Views</p>
//                 <p className="text-2xl font-bold text-gray-900">1,234</p>
//               </div>
//               <User className="text-blue-600" size={40} />
//             </div>
//           </div>

//           <div className="bg-white rounded-lg shadow p-6">
//             <div className="flex items-center justify-between">
//               <div>
//                 <p className="text-sm text-gray-600">Applications</p>
//                 <p className="text-2xl font-bold text-gray-900">45</p>
//               </div>
//               <Briefcase className="text-green-600" size={40} />
//             </div>
//           </div>

//           <div className="bg-white rounded-lg shadow p-6">
//             <div className="flex items-center justify-between">
//               <div>
//                 <p className="text-sm text-gray-600">Saved Jobs</p>
//                 <p className="text-2xl font-bold text-gray-900">12</p>
//               </div>
//               <Heart className="text-red-600" size={40} />
//             </div>
//           </div>
//         </div>

//         {/* Recent Activity */}
//         <div className="bg-white rounded-lg shadow p-6">
//           <h3 className="text-xl font-bold text-gray-900 mb-4">Recent Activity</h3>
//           <div className="space-y-4">
//             {[1, 2, 3].map((item) => (
//               <div key={item} className="flex items-center justify-between p-4 bg-gray-50 rounded-lg">
//                 <div>
//                   <p className="font-medium text-gray-900">Applied to Senior Developer</p>
//                   <p className="text-sm text-gray-600">at Tech Company Inc.</p>
//                 </div>
//                 <span className="text-sm text-gray-500">2 hours ago</span>
//               </div>
//             ))}
//           </div>
//         </div>
//       </main>
//     </div>
//   );
// };

// export default DashboardPage;