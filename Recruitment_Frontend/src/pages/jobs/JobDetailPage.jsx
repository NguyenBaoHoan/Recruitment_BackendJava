import { useState, useEffect } from "react";
import { useParams, useNavigate, Link } from "react-router-dom";
import MainLayout from "../../components/layout/MainLayout";

const JobDetailPage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [job, setJob] = useState(null);
    const [loading, setLoading] = useState(true);
    const [relatedJobs, setRelatedJobs] = useState([]);

    // Mock data - trong thực tế bạn sẽ fetch từ API
    const allJobs = [
        {
            id: 1,
            name: "Frontend Developer (ReactJS) - Senior Level với kinh nghiệm từ 5 năm trở lên",
            location: "Hồ Chí Minh",
            salary: "20,000,000 - 30,000,000 VNĐ",
            job_type: "Full-time",
            work_address: "Tòa nhà ABC, Quận 1, TP.HCM",
            start_date: "2025-01-01",
            end_date: "2025-02-15",
            is_active: true,
            description: "Chúng tôi đang tìm kiếm một Frontend Developer có kinh nghiệm với ReactJS để tham gia vào đội ngũ phát triển sản phẩm. Bạn sẽ làm việc trên các dự án thú vị và có cơ hội phát triển nghề nghiệp.",
            requirements: [
                "5+ năm kinh nghiệm phát triển Frontend",
                "Thành thạo ReactJS, TypeScript",
                "Kinh nghiệm với Redux, React Query",
                "Hiểu biết về HTML5, CSS3, Sass",
                "Kinh nghiệm với testing (Jest, React Testing Library)"
            ],
            benefits: [
                "Lương tháng 13, thưởng hiệu suất",
                "Bảo hiểm sức khỏe cao cấp",
                "Đào tạo và phát triển kỹ năng",
                "Du lịch công ty hàng năm",
                "Làm việc remote linh hoạt"
            ],
            company: "Công ty Cổ phần Công nghệ ABC",
            contact_email: "hr@abc-tech.com",
            contact_phone: "028 1234 5678"
        },
        {
            id: 2,
            name: "Backend Developer (Spring Boot, Microservices, Cloud AWS) - Chuyên viên cao cấp",
            location: "Hà Nội",
            salary: "25,000,000 - 35,000,000 VNĐ",
            job_type: "Full-time",
            work_address: "Tòa nhà XYZ, Quận Cầu Giấy, Hà Nội",
            start_date: "2025-02-01",
            end_date: "2025-03-10",
            is_active: false,
            description: "Tham gia vào đội ngũ phát triển backend với kiến trúc microservices và cloud-native. Xây dựng các hệ thống có khả năng mở rộng cao.",
            requirements: [
                "4+ năm kinh nghiệm Java/Spring Boot",
                "Kinh nghiệm với Microservices",
                "AWS hoặc cloud platform khác",
                "Database: MySQL, PostgreSQL",
                "Docker, Kubernetes"
            ],
            benefits: [
                "Môi trường làm việc chuyên nghiệp",
                "Tham gia conference",
                "Macbook Pro",
                "Phụ cấp ăn trưa",
                "Club thể thao"
            ],
            company: "Công ty TNHH XYZ",
            contact_email: "tuyendung@xyz.com",
            contact_phone: "024 8765 4321"
        },
        // ... thêm các job khác từ AllJobsPage
        {
            id: 3,
            name: "UI/UX Designer",
            location: "Đà Nẵng",
            salary: "15,000,000 - 22,000,000 VNĐ",
            job_type: "Part-time",
            work_address: "123 Lê Duẩn, Đà Nẵng",
            start_date: "2025-01-15",
            end_date: "2025-02-28",
            is_active: true,
            description: "Thiết kế giao diện người dùng và trải nghiệm người dùng cho các sản phẩm digital.",
            requirements: [
                "2+ năm kinh nghiệm UI/UX",
                "Thành thạo Figma, Adobe XD",
                "Hiểu biết về design system",
                "Portfolio thể hiện rõ quy trình design"
            ],
            benefits: [
                "Làm việc linh hoạt",
                "Tham gia workshop design",
                "Budget mua sách/source",
                "Device hỗ trợ"
            ],
            company: "Studio Sáng tạo DEF",
            contact_email: "hello@def-studio.com",
            contact_phone: "0236 123 4567"
        }
    ];

    useEffect(() => {
        // Simulate API call
        const fetchJobDetail = () => {
            setLoading(true);
            setTimeout(() => {
                const foundJob = allJobs.find(job => job.id === parseInt(id));
                setJob(foundJob);

                // Find related jobs (same location or job type)
                if (foundJob) {
                    const related = allJobs
                        .filter(j => j.id !== foundJob.id &&
                            (j.location === foundJob.location || j.job_type === foundJob.job_type))
                        .slice(0, 3);
                    setRelatedJobs(related);
                }

                setLoading(false);
            }, 500);
        };

        fetchJobDetail();
    }, [id]);

    if (loading) {
        return (
            <MainLayout>
                <div className="min-h-screen bg-gray-50 py-8">
                    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8">
                        <div className="animate-pulse">
                            <div className="h-8 bg-gray-200 rounded w-1/3 mb-4"></div>
                            <div className="h-4 bg-gray-200 rounded w-1/4 mb-8"></div>
                            <div className="space-y-4">
                                <div className="h-4 bg-gray-200 rounded"></div>
                                <div className="h-4 bg-gray-200 rounded w-5/6"></div>
                                <div className="h-4 bg-gray-200 rounded w-4/6"></div>
                            </div>
                        </div>
                    </div>
                </div>
            </MainLayout>
        );
    }

    if (!job) {
        return (
            <MainLayout>
                <div className="min-h-screen bg-gray-50 py-8">
                    <div className="max-w-4xl mx-auto px-4 sm:px-6 lg:px-8 text-center">
                        <div className="bg-white rounded-xl shadow-sm p-8">
                            <svg className="w-16 h-16 text-gray-400 mx-auto mb-4" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={1} d="M9.172 16.172a4 4 0 015.656 0M9 10h.01M15 10h.01M21 12a9 9 0 11-18 0 9 9 0 0118 0z" />
                            </svg>
                            <h2 className="text-2xl font-bold text-gray-900 mb-4">Không tìm thấy công việc</h2>
                            <p className="text-gray-600 mb-6">Công việc bạn đang tìm kiếm không tồn tại hoặc đã bị xóa.</p>
                            <button
                                onClick={() => navigate("/jobs")}
                                className="bg-blue-600 hover:bg-blue-700 text-white px-6 py-3 rounded-lg transition"
                            >
                                Quay lại danh sách
                            </button>
                        </div>
                    </div>
                </div>
            </MainLayout>
        );
    }

    return (
        <MainLayout>
            <div className="min-h-screen bg-gray-50 py-8">
                <div className="max-w-6xl mx-auto px-4 sm:px-6 lg:px-8">
                    {/* Breadcrumb */}
                    <nav className="flex mb-6" aria-label="Breadcrumb">
                        <ol className="flex items-center space-x-2 text-sm">
                            <li>
                                <Link to="/jobs" className="text-gray-500 hover:text-gray-700 transition">
                                    Danh sách việc làm
                                </Link>
                            </li>
                            <li className="flex items-center">
                                <svg className="w-4 h-4 text-gray-400" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                                </svg>
                                <span className="ml-2 text-gray-900 font-medium truncate max-w-xs">
                                    {job.name}
                                </span>
                            </li>
                        </ol>
                    </nav>

                    <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                        {/* Main Content */}
                        <div className="lg:col-span-2">
                            <div className="bg-white rounded-xl shadow-sm border border-gray-200 overflow-hidden">
                                {/* Header */}
                                <div className="p-6 border-b border-gray-200">
                                    <div className="flex justify-between items-start mb-4">
                                        <div className="flex-1">
                                            <h1 className="text-2xl lg:text-3xl font-bold text-gray-900 mb-2">
                                                {job.name}
                                            </h1>
                                            <div className="flex flex-wrap gap-2 mb-4">
                                                <span className={`px-3 py-1 text-sm font-medium rounded-full ${job.is_active
                                                    ? "bg-green-100 text-green-800 border border-green-200"
                                                    : "bg-red-100 text-red-800 border border-red-200"
                                                    }`}>
                                                    {job.is_active ? "Đang tuyển" : "Hết hạn"}
                                                </span>
                                                <span className="px-3 py-1 bg-blue-100 text-blue-800 text-sm font-medium rounded-full border border-blue-200">
                                                    {job.job_type}
                                                </span>
                                            </div>
                                        </div>
                                    </div>

                                    <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                        <div className="flex items-center gap-3">
                                            <div className="w-10 h-10 bg-blue-100 rounded-lg flex items-center justify-center flex-shrink-0">
                                                <svg className="w-5 h-5 text-blue-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17.657 16.657L13.414 20.9a1.998 1.998 0 01-2.827 0l-4.244-4.243a8 8 0 1111.314 0z" />
                                                </svg>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Địa điểm</p>
                                                <p className="font-medium text-gray-900">{job.location}</p>
                                            </div>
                                        </div>

                                        <div className="flex items-center gap-3">
                                            <div className="w-10 h-10 bg-green-100 rounded-lg flex items-center justify-center flex-shrink-0">
                                                <svg className="w-5 h-5 text-green-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8c-1.657 0-3 .895-3 2s1.343 2 3 2 3 .895 3 2-1.343 2-3 2m0-8c1.11 0 2.08.402 2.599 1M12 8V7m0 1v8m0 0v1m0-1c-1.11 0-2.08-.402-2.599-1" />
                                                </svg>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Mức lương</p>
                                                <p className="font-medium text-green-600">{job.salary}</p>
                                            </div>
                                        </div>

                                        <div className="flex items-center gap-3">
                                            <div className="w-10 h-10 bg-purple-100 rounded-lg flex items-center justify-center flex-shrink-0">
                                                <svg className="w-5 h-5 text-purple-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M21 13.255A23.931 23.931 0 0112 15c-3.183 0-6.22-.62-9-1.745M16 6V4a2 2 0 00-2-2h-4a2 2 0 00-2 2v2m4 6h.01M5 20h14a2 2 0 002-2V8a2 2 0 00-2-2H5a2 2 0 00-2 2v10a2 2 0 002 2z" />
                                                </svg>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Loại hình</p>
                                                <p className="font-medium text-gray-900">{job.job_type}</p>
                                            </div>
                                        </div>

                                        <div className="flex items-center gap-3">
                                            <div className="w-10 h-10 bg-orange-100 rounded-lg flex items-center justify-center flex-shrink-0">
                                                <svg className="w-5 h-5 text-orange-600" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M8 7V3m8 4V3m-9 8h10M5 21h14a2 2 0 002-2V7a2 2 0 00-2-2H5a2 2 0 00-2 2v12a2 2 0 002 2z" />
                                                </svg>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Thời hạn</p>
                                                <p className="font-medium text-gray-900">{job.end_date}</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>

                                {/* Job Details */}
                                <div className="p-6 space-y-8">
                                    {/* Description */}
                                    <div>
                                        <h2 className="text-xl font-semibold text-gray-900 mb-4">Mô tả công việc</h2>
                                        <div className="prose max-w-none text-gray-700 leading-relaxed">
                                            <p>{job.description}</p>
                                        </div>
                                    </div>

                                    {/* Requirements */}
                                    <div>
                                        <h2 className="text-xl font-semibold text-gray-900 mb-4">Yêu cầu công việc</h2>
                                        <ul className="space-y-2">
                                            {job.requirements?.map((requirement, index) => (
                                                <li key={index} className="flex items-start gap-3">
                                                    <svg className="w-5 h-5 text-green-500 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M5 13l4 4L19 7" />
                                                    </svg>
                                                    <span className="text-gray-700">{requirement}</span>
                                                </li>
                                            ))}
                                        </ul>
                                    </div>

                                    {/* Benefits */}
                                    <div>
                                        <h2 className="text-xl font-semibold text-gray-900 mb-4">Quyền lợi</h2>
                                        <ul className="space-y-2">
                                            {job.benefits?.map((benefit, index) => (
                                                <li key={index} className="flex items-start gap-3">
                                                    <svg className="w-5 h-5 text-blue-500 mt-0.5 flex-shrink-0" fill="none" stroke="currentColor" viewBox="0 0 24 24">
                                                        <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M12 8v4l3 3m6-3a9 9 0 11-18 0 9 9 0 0118 0z" />
                                                    </svg>
                                                    <span className="text-gray-700">{benefit}</span>
                                                </li>
                                            ))}
                                        </ul>
                                    </div>

                                    {/* Contact Information */}
                                    <div className="bg-blue-50 rounded-lg p-6">
                                        <h2 className="text-xl font-semibold text-gray-900 mb-4">Thông tin liên hệ</h2>
                                        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                                            <div>
                                                <p className="text-sm text-gray-600">Công ty</p>
                                                <p className="font-medium text-gray-900">{job.company}</p>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Email</p>
                                                <p className="font-medium text-gray-900">{job.contact_email}</p>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Điện thoại</p>
                                                <p className="font-medium text-gray-900">{job.contact_phone}</p>
                                            </div>
                                            <div>
                                                <p className="text-sm text-gray-600">Địa chỉ làm việc</p>
                                                <p className="font-medium text-gray-900">{job.work_address}</p>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        {/* Sidebar */}
                        <div className="space-y-6">
                            {/* Apply Card */}
                            {job.is_active && (
                                <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                                    <h3 className="text-lg font-semibold text-gray-900 mb-4">Ứng tuyển ngay</h3>
                                    <button className="w-full bg-blue-600 hover:bg-blue-700 text-white py-3 px-4 rounded-lg font-medium transition mb-3">
                                        Nộp hồ sơ online
                                    </button>
                                    <button className="w-full border border-gray-300 hover:bg-gray-50 text-gray-700 py-3 px-4 rounded-lg font-medium transition">
                                        Lưu công việc
                                    </button>
                                </div>
                            )}

                            {/* Job Summary */}
                            <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                                <h3 className="text-lg font-semibold text-gray-900 mb-4">Thông tin chung</h3>
                                <div className="space-y-3">
                                    <div className="flex justify-between">
                                        <span className="text-gray-600">Ngày bắt đầu:</span>
                                        <span className="font-medium">{job.start_date}</span>
                                    </div>
                                    <div className="flex justify-between">
                                        <span className="text-gray-600">Ngày kết thúc:</span>
                                        <span className="font-medium">{job.end_date}</span>
                                    </div>
                                    <div className="flex justify-between">
                                        <span className="text-gray-600">Trạng thái:</span>
                                        <span className={`font-medium ${job.is_active ? 'text-green-600' : 'text-red-600'}`}>
                                            {job.is_active ? 'Đang tuyển' : 'Hết hạn'}
                                        </span>
                                    </div>
                                </div>
                            </div>

                            {/* Related Jobs */}
                            {relatedJobs.length > 0 && (
                                <div className="bg-white rounded-xl shadow-sm border border-gray-200 p-6">
                                    <h3 className="text-lg font-semibold text-gray-900 mb-4">Công việc liên quan</h3>
                                    <div className="space-y-4">
                                        {relatedJobs.map(relatedJob => (
                                            <div
                                                key={relatedJob.id}
                                                onClick={() => navigate(`/jobs/${relatedJob.id}`)}
                                                className="p-3 border border-gray-200 rounded-lg hover:border-blue-300 hover:shadow-md transition cursor-pointer"
                                            >
                                                <h4 className="font-medium text-gray-900 mb-1 line-clamp-2">
                                                    {relatedJob.name}
                                                </h4>
                                                <p className="text-sm text-green-600 mb-1">{relatedJob.salary}</p>
                                                <p className="text-xs text-gray-500">{relatedJob.location}</p>
                                            </div>
                                        ))}
                                    </div>
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </MainLayout>
    );
};

export default JobDetailPage;