import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { RouteConstants } from '../../../../constants/route-constants';
import { UsuarioService } from '../../../../services/usuario-service';
import { UsuarioDTO } from '../../../../model/usuario-dto';

@Component({
  selector: 'app-usuario-detalhe',
  imports: [],
  templateUrl: './usuario-detalhe.component.html',
  styleUrl: './usuario-detalhe.component.css',
  providers: [UsuarioService],
})
export class UsuarioDetalheComponent {
  modoEdicao: boolean = false;
  usuario: UsuarioDTO = {} as UsuarioDTO;

  constructor(private route: ActivatedRoute, private service: UsuarioService) {
    const id = this.route.snapshot.paramMap.get('id');

    if (id === RouteConstants.P_ADD) {
      this.modoEdicao = false;
    } else {
      this.modoEdicao = true;
      this.service.findById(id!).subscribe((response) => {
        this.usuario = response.body;
      });
    }
  }
}
