export default function Loading() {
    return (
        <div className="fixed inset-0 bg-black/50 flex items-center justify-center z-50">
                <svg
                    className="animate-spin h-12 w-12 text-green-500 mb-4"
                    xmlns="http://www.w3.org/2000/svg"
                    fill="none"
                    viewBox="0 0 24 24"
                >
                    <circle
                        className="opacity-25"
                        cx="12"
                        cy="12"
                        r="10"
                        stroke="white"
                        strokeWidth="4"
                    />
                    <path
                        className="opacity-75"
                        fill="white"
                        d="M4 12a8 8 0 018-8v4a4 4 0 00-4 4H4z"
                    />
                </svg>
                <div className="text-xl font-semibold text-white-700">Loading...</div>
        </div>
    );
}