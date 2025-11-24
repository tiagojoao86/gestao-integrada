import { Component, EventEmitter, inject, Input, OnInit, Output } from '@angular/core';
import { RouteConstants } from '../../../base/constants/route-constants';
import { PerfilService } from '../perfil.service';
import { RegisterActionToolbar, BaseComponent } from '../../../base/base.component';
import { CommonModule } from '@angular/common';
import { IftaLabelModule } from 'primeng/iftalabel';
import { AbstractControl, FormArray, FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { InputTextModule } from 'primeng/inputtext';
import { MessageService } from '../../../base/messages/messages.service';
import { PerfilDTO } from '../model/perfil-dto';
import { ModuloService } from '../modulo.service';
import { ModuloDTO } from '../model/modulo-dto';
import { CheckboxModule } from 'primeng/checkbox';
import { AccordionModule } from 'primeng/accordion';
import { PerfilModuloDTO } from '../model/perfil-modulo-dto';
import { CheckboxChangeEvent } from 'primeng/checkbox';
import { Response } from '../../../base/model/response'; 

interface PermissaoFormGroupValue {
  id?: string;
  perfilId: string;
  moduloId: string;
  moduloNome: string;
  chave: string;
  grupo: string;
  selecionado: boolean;
  podeListar: boolean;
  podeVisualizar: boolean;
  podeEditar: boolean;
  podeDeletar: boolean;
}

@Component({
  selector: 'gi-perfil-detalhe',
  standalone: true,
  imports: [
    CommonModule,
    BaseComponent,
    IftaLabelModule,
    ReactiveFormsModule,
    InputTextModule,
    CheckboxModule,
    AccordionModule
  ],
  templateUrl: './perfil-detalhe.component.html',
  styleUrl: './perfil-detalhe.component.css',
  providers: [PerfilService, ModuloService],
})
export class PerfilDetalheComponent implements OnInit {
  form: FormGroup = new FormGroup([]);
  modoEdicao = false;
  perfil: PerfilDTO = { id: '', nome: '', permissoes: [] };
  modulosAgrupados: Record<string, ModuloDTO[]> = {};
  @Input() detailId: string | number | null = null;
  @Output() closeDetail = new EventEmitter<void>();

  private service: PerfilService = inject(PerfilService);
  private messages: MessageService = inject(MessageService);
  private moduloService: ModuloService = inject(ModuloService);
  private fb = inject(FormBuilder).nonNullable;

  titulo = $localize`Perfil: `;

  acoesTela: RegisterActionToolbar[] = [
    { action: () => this.goBackFn(), icon: 'close', title: $localize`Cancelar` + ' (esc)', shortcut: 'escape' },
    { action: () => this.salvar(), icon: 'save', title: $localize`Salvar` + ' (enter)', shortcut: 'enter' },
  ];

  get permissoes(): FormArray {
    return this.form.get('permissoes') as FormArray;
  }

  ngOnInit(): void {
    this.initForm();
    this.loadData();
  }

  private loadData(): void {
    this.moduloService.getGroupedModules().subscribe((response: Response) => {
      const modulos = response.body as Record<string, ModuloDTO[]>; 
      for (const grupo in modulos) {
        modulos[grupo].sort((a: ModuloDTO, b: ModuloDTO) => a.nome.localeCompare(b.nome));
      }
      this.modulosAgrupados = modulos;
      
      if (this.detailId === RouteConstants.P_ADD) {
        this.modoEdicao = false;
        this.titulo += $localize`Novo`;
        this.buildPermissoesForm();
      } else {
        this.modoEdicao = true;
        this.service.findById(String(this.detailId!)).subscribe(response => {          
          this.perfil = response.body as PerfilDTO;
          this.titulo += this.perfil.nome;
          this.fillForm();
          this.buildPermissoesForm();
        });
      }
    });
  }

  private buildPermissoesForm(): void {
    const allModules = Object.values(this.modulosAgrupados).flat();
    allModules.forEach(modulo => {
      if (modulo) {        
        const permissaoExistente = this.perfil.permissoes.find(p => p.moduloId === modulo.id);
        this.permissoes.push(this.createPermissaoGroup(modulo, permissaoExistente));
      }
    });
  }

  private createPermissaoGroup(modulo: ModuloDTO, permissao?: PerfilModuloDTO): FormGroup {
    return this.fb.group({
      id: [permissao?.id || null],
      perfilId: [this.perfil.id],
      moduloId: [modulo.id],
      moduloNome: [modulo.nome],
      chave: [modulo.chave],
      grupo: [modulo.grupoEnum],
      selecionado: [!!permissao],
      podeListar: [permissao?.podeListar || false],
      podeVisualizar: [permissao?.podeVisualizar || false],
      podeEditar: [permissao?.podeEditar || false],
      podeDeletar: [permissao?.podeDeletar || false],
    });
  }

  initForm(): void {
    this.form = this.fb.group({
      nome: [null, [Validators.required]],
      permissoes: this.fb.array([])
    });
  }

  fillForm(): void {
    this.form.get('nome')?.setValue(this.perfil.nome);    
  }

  salvar(): void {
    if (!this.form.valid) {
      this.messages.erro($localize`Existem campos inválidos.`);
      return;
    }
  
    const payloadPermissoes: PerfilModuloDTO[] = this.permissoes.value
      .filter((p: PermissaoFormGroupValue) => p.podeListar || p.podeVisualizar || p.podeEditar || p.podeDeletar)
      .map((p: PermissaoFormGroupValue): PerfilModuloDTO => {
        return {
          id: p.id,
          perfilId: p.perfilId,
          moduloId: p.moduloId,
          moduloNome: p.moduloNome,
          podeListar: p.podeListar,
          podeVisualizar: p.podeVisualizar,
          podeEditar: p.podeEditar,
          podeDeletar: p.podeDeletar,
        };
      });

    const payload: PerfilDTO = {
      ...this.form.value,
      id: this.perfil.id,
      permissoes: payloadPermissoes,
    };

    console.log('[DEBUG] Payload enviado ao backend:', payload);
  
    this.service.save(payload, {
      onSuccess: (data: PerfilDTO) => {
        this.perfil = data;
        this.messages.sucesso($localize`Perfil salvo com sucesso.`);
        this.goBackFn();
      },
    });
  }

  isControlInvalid(campo: string): boolean {
    const fc: AbstractControl | null = this.form.get(campo);
    return !!(fc && fc.invalid && (fc.touched || fc.dirty));
  }

  goBackFn = (): void => {
    this.closeDetail.emit();
  };

  getGrupos(): string[] {
    return Object.keys(this.modulosAgrupados).sort();
  }

  getPermissoesDoGrupo(grupo: string): AbstractControl[] {
    return this.permissoes.controls.filter(
      (control) => control.get('grupo')?.value === grupo
    );
  }

  onSelecaoModuloChange(permissaoGroup: AbstractControl, event: CheckboxChangeEvent): void {
    const selecionado = event.checked;
    if (!selecionado) {
      permissaoGroup.get('podeListar')?.setValue(false);
      permissaoGroup.get('podeVisualizar')?.setValue(false);
      permissaoGroup.get('podeEditar')?.setValue(false);
      permissaoGroup.get('podeDeletar')?.setValue(false);
    } else {
      permissaoGroup.get('podeListar')?.setValue(true);
      permissaoGroup.get('podeVisualizar')?.setValue(true);
      permissaoGroup.get('podeEditar')?.setValue(true);
      permissaoGroup.get('podeDeletar')?.setValue(true);
    }
  }
}
