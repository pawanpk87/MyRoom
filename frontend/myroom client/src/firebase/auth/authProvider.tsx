import { useEffect, ReactNode } from "react";
import React from "react";
import { auth } from "../config";
import {
  GoogleAuthProvider,
  User,
  onAuthStateChanged,
  signInWithPopup,
  signOut,
} from "firebase/auth";
import { setCookie } from "cookies-next";

interface AuthProviderProps {
  children: ReactNode;
}

interface AuthContextProps {
  user: User | null;
  googleSignIn: () => void;
  logOut: () => void;
}

const UserAuthContext: React.Context<AuthContextProps> =
  React.createContext<AuthContextProps>({
    user: null,
    googleSignIn: () => {},
    logOut: () => {},
  });

export const UserAuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = React.useState<User | null>(null);

  const googleSignIn = () => {
    const provider = new GoogleAuthProvider();
    signInWithPopup(auth, provider);
  };

  const logOut = () => {
    signOut(auth);
  };

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      if (user) {
        setUser(user);
        console.log("user is");
        console.log(user);
        setCookie("userData", user.toJSON());
      }
    });
    return () => unsubscribe();
  }, []);

  return (
    <UserAuthContext.Provider value={{ user, googleSignIn, logOut }}>
      {children}
    </UserAuthContext.Provider>
  );
};

export const useUserAuth = (): AuthContextProps => {
  return React.useContext(UserAuthContext);
};
