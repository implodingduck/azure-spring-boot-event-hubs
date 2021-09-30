terraform {
  required_providers {
    azurerm = {
      source  = "hashicorp/azurerm"
      version = "=2.71.0"
    }
    random = {
      source  = "hashicorp/random"
      version = "=3.1.0"
    }
  }
  backend "azurerm" {

  }
}

provider "azurerm" {
  features {}

  subscription_id = var.subscription_id
}

locals {
  func_name = "quackbank${random_string.unique.result}"
}

data "azurerm_client_config" "current" {}

data "azurerm_log_analytics_workspace" "default" {
  name                = "DefaultWorkspace-${data.azurerm_client_config.current.subscription_id}-EUS"
  resource_group_name = "DefaultResourceGroup-EUS"
} 

resource "azurerm_resource_group" "rg" {
  name     = "rg-springboot-eventhubs-${random_string.unique.result}"
  location = var.location
}

resource "random_string" "unique" {
  length  = 8
  special = false
  upper   = false
}

resource "azurerm_eventhub_namespace" "ns" {
  name                     = "ns-sb-eh-${random_string.unique.result}"
  resource_group_name      = azurerm_resource_group.rg.name
  location                 = azurerm_resource_group.rg.location
  sku                      = "Standard"
  capacity                 = 1
  auto_inflate_enabled     = true
  maximum_throughput_units = 20

  tags = {
    managed_by = "terraform"
  }
}

# Consumer topic

resource "azurerm_eventhub" "topic" {
  name                = "topic-sb-eh-${random_string.unique.result}"
  namespace_name      = azurerm_eventhub_namespace.ns.name
  resource_group_name = azurerm_eventhub_namespace.ns.resource_group_name
  partition_count     = 2
  message_retention   = 1
}

resource "azurerm_eventhub_consumer_group" "cg" {
  name                = "cg-sb-eh-${random_string.unique.result}"
  namespace_name      = azurerm_eventhub_namespace.ns.name
  eventhub_name       = azurerm_eventhub.topic.name
  resource_group_name = azurerm_eventhub_namespace.ns.resource_group_name
}

resource "azurerm_storage_account" "sa" {
  name                     = "sasbeh${random_string.unique.result}"
  resource_group_name      = azurerm_resource_group.rg.name
  location                 = azurerm_resource_group.rg.location
  account_kind             = "StorageV2"
  account_tier             = "Standard"
  account_replication_type = "LRS"
}

resource "azurerm_storage_container" "checkpoint" {
  name                  = "checkpoint"
  storage_account_name  = azurerm_storage_account.sa.name
  container_access_type = "private"
}