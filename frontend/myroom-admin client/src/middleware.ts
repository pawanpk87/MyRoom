import { NextRequest, NextResponse } from "next/server";
import { verfiyToken } from "./services/myRoom/authService/authService";

export async function middleware(request: NextRequest) {
  const cookies = request.cookies;

  if (!cookies || !cookies.has("userData") || !cookies.get("userData")?.value) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  const userData = JSON.parse(cookies.get("userData")?.value!);
  const accessToken = userData.stsTokenManager.accessToken;

  const valid: boolean = await verfiyToken(accessToken);

  if (!valid) {
    return NextResponse.redirect(new URL("/login", request.url));
  }

  return NextResponse.next();
}

export const config = {
  matcher: ["/dashboard/:path*", "/onboarding"],
};
