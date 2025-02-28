export interface UserAppDTO {
  id: string;
  name: string;
  username: string;
  password: string;
  createdat?: Date;
  updatedAt?: Date;
  createdBy?: string;
  updatedBy?: string;
}
