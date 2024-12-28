import { CommonModule } from "@angular/common";
import { Component, EventEmitter, Input, OnInit, Output } from "@angular/core";
import { Ordem, OrdemDirecao } from "../../../model/requisicao-paginada";
import { TableModule } from 'primeng/table';
import { ButtonModule } from 'primeng/button';

@Component({
    selector: 'table-component',
    imports: [CommonModule, TableModule, ButtonModule],
    templateUrl: './table.component.html',
    styleUrl: './table.component.css',
    providers: []
})
export class TableComponent implements OnInit { 
    @Input('dataSource') dataSource: any[] = [];
    @Input('columnDefinition') columnDefinition: DataSourceColumn[] = [];
    @Input('actions') actions: Action[] = [];

    @Output('sortingEvent') sortingEvent: EventEmitter<Ordem[]> = new EventEmitter();;

    columns: string[] = [];

    constructor() {}

    ngOnInit(): void {        
        this.columns = this.columnDefinition.map(it => it.name);
        
        if (this.actions) {
            this.columns.push("actions");
        }
    }

    sortChange(sortState: any) {        
        let ordem: Ordem[] = [];

        sortState.multisortmeta.forEach((sort: any) => {
            let propriedade = sort.field;
            let direcao = sort.order === 1 ? OrdemDirecao.ASC : OrdemDirecao.DESC;

            ordem.push({ propriedade, direcao });
        });
        

        this.sortingEvent.emit(ordem);
    }
}

export interface DataSourceColumn {
    name: string;
    label: string;
    getValue: Function;
}

export interface Action {
    icon: string;
    action: Function;
}