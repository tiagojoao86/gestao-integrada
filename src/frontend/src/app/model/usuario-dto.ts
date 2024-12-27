export interface UsuarioDTO {
    id: string;
    nome: string;
    login: string;
    criadoEm: Date;
    atualizadoEm?: Date;
    criadoPor: string;
    atualizadoPor?: string;    
}