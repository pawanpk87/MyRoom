import React, { useEffect, ReactNode, useState } from "react";
import { auth } from "../config";
import {
  GoogleAuthProvider,
  User,
  onAuthStateChanged,
  signInWithPopup,
  signInWithRedirect,
  signOut,
} from "firebase/auth";
import { setCookie } from "cookies-next";
import { organizations } from "@/typings";
import organizationService from "@/services/myRoom/organization/organizationService";

interface AuthProviderProps {
  children: ReactNode;
}

interface AuthContextProps {
  user: User | null;
  googleSignIn: () => void;
  logOut: () => void;
  organization: organizations.IOrganization | null;
  isOrganizationDataLoading: boolean;
}

const UserAuthContext: React.Context<AuthContextProps> =
  React.createContext<AuthContextProps>({
    user: null,
    googleSignIn: () => {},
    logOut: () => {},
    organization: null,
    isOrganizationDataLoading: false,
  });

export const UserAuthProvider: React.FC<AuthProviderProps> = ({ children }) => {
  const [user, setUser] = React.useState<User | null>(null);
  const [organization, setOrganization] =
    React.useState<organizations.IOrganization | null>(null);
  const [isOrganizationDataLoading, setIsOrganizationDataLoading] =
    React.useState<boolean>(false);

  const googleSignIn = () => {
    const provider = new GoogleAuthProvider();
    signInWithPopup(auth, provider);
  };

  const logOut = () => {
    signOut(auth);
  };

  const getOrganization = (uid: string) => {
    setIsOrganizationDataLoading(true);
    organizationService
      .getOrganizationBySuperAdminId(uid)
      .then((data) => {
        setOrganization(data.data);
        setIsOrganizationDataLoading(false);
      })
      .catch((error) => {
        setIsOrganizationDataLoading(false);
      });
  };

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      if (user) {
        setUser(user);
        getOrganization(user.uid);
        setCookie("userData", user.toJSON());
      }
    });
    return () => unsubscribe();
  }, []);

  return (
    <UserAuthContext.Provider
      value={{
        user,
        googleSignIn,
        logOut,
        organization,
        isOrganizationDataLoading,
      }}
    >
      {children}
    </UserAuthContext.Provider>
  );
};

export const useUserAuth = (): AuthContextProps => {
  return React.useContext(UserAuthContext);
};
