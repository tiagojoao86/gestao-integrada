import { CommonModule } from "@angular/common";
import { AfterViewInit, Component, EventEmitter, Input, LOCALE_ID, OnInit, Output, ViewChild } from "@angular/core";
import { MatButtonModule } from "@angular/material/button";
import { MatIconModule } from "@angular/material/icon";
import {MatSort, Sort, MatSortModule} from '@angular/material/sort';
import { MatTableModule } from "@angular/material/table";
import { Ordem, OrdemDirecao } from "../../../model/requisicao-paginada";

@Component({
    selector: 'table-component',
    imports: [CommonModule, MatTableModule, MatIconModule, MatButtonModule, MatSortModule],
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

    sortChange(sortState: Sort) {
        let propriedade = sortState.active;
        let direcao = sortState.direction === 'asc' ? OrdemDirecao.ASC : OrdemDirecao.DESC;

        this.sortingEvent.emit([{ propriedade, direcao }]);
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