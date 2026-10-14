
const BASE_URL = process.env.NEXT_PUBLIC_API_URL;

export async function apiFetch(
  path: string,
  option: RequestInit = {}
): Promise<Response> {
  const token = localStorage.getItem("token");

  const res = await fetch(`${BASE_URL}${path}`, {
    ...option,
    headers: {
      "Content-Type": "application/json",
      Authentication: token ? `Bearer ${token}` : "",
    },
  });

  if (res.status === 401) {
    localStorage.removeItem("token"); 
  }

  return res;
}